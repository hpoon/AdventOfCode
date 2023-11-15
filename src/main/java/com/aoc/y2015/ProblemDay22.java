package com.aoc.y2015;

import com.aoc.ProblemDay;
import com.google.common.collect.ImmutableList;
import lombok.*;

import java.util.*;

public class ProblemDay22 extends ProblemDay<Integer, Integer> {

    private static final Spell MAGIC_MISSILE = new Spell(SpellName.MAGIC_MISSILE, 53, 4, 0, 0, 0, 0);
    private static final Spell DRAIN = new Spell(SpellName.DRAIN, 73, 2, 0, 2, 0, 0);
    private static final Spell SHIELD = new Spell(SpellName.SHIELD, 113, 0, 7, 0, 0, 6);
    private static final Spell POISON = new Spell(SpellName.POISON, 173, 3, 0, 0, 0, 6);
    private static final Spell RECHARGE = new Spell(SpellName.RECHARGE, 229, 0, 0, 0, 101, 5);
    private static final List<Spell> SPELLS = ImmutableList.of(MAGIC_MISSILE, DRAIN, SHIELD, POISON, RECHARGE);

    @Override
    protected Integer solveA() {
        return solve(true);
    }

    @Override
    protected Integer solveB() {
        return solve(false);
    }

    private int solve(boolean easyMode) {
        Queue<State> queue = new LinkedList<>();
        Player player = new Player(50, 500, 0, 0, 0, 0);
        Boss boss = parse();
        State start = new State(player, boss);
        queue.add(start);
        int best = 99999999;
        Map<State, State> trail = new HashMap<>();
        while (!queue.isEmpty()) {
            State state = queue.poll();
            if (state.getPlayer().getManaUsed() >= best) {
                continue;
            }
            for (Spell spell : SPELLS) {
                Player p = state.getPlayer().copy();
                Boss b = state.getBoss().copy();
                Player w = takeTurn(easyMode, spell, p, b, state, trail, queue);
                if (w != null) {
                    if (p.getManaUsed() < best) {
                        best = p.getManaUsed();
                    }
                }
            }
        }
        return best;
    }

    private static Player takeTurn(boolean easyMode,
                                   Spell spell,
                                   Player p,
                                   Boss b,
                                   State state,
                                   Map<State, State> trail,
                                   Queue<State> queue) {
        if (!easyMode) {
            p.takeHardModeDmg();
            if (p.dead()) {
                return null;
            }
        }
        if (!p.hasMana(spell.getCost())) {
            return null;
        }
        p.useMana(spell.getCost());
        p.rechargeIfPossible();
        int dmg = p.getPoisonTurns() > 0 ? POISON.getDmg() : 0;
        switch (spell.getName()) {
            case MAGIC_MISSILE:
                dmg += spell.getDmg();
                break;
            case DRAIN:
                dmg += spell.getDmg();
                p.heal(spell.getHeal());
                break;
            case SHIELD:
                if (!p.isShieldActive()) {
                    p.activateShield();
                }
                break;
            case POISON:
                if (!p.isPoisonActive()) {
                    p.activatePoison();
                }
                break;
            case RECHARGE:
                if (!p.isRechargeActive()) {
                    p.activateRecharge();
                }
                break;
        }
        b.takeDamage(dmg);
        if (b.dead()) {
            return p.copy();
        }
        p.turnEffects();
        b.takeDamage(p.getPoisonTurns() > 0 ? POISON.getDmg() : 0);
        if (b.dead()) {
            return p.copy();
        }
        p.takeDamage(b.getAtk());
        p.rechargeIfPossible();
        p.turnEffects();
        if (p.dead()) {
            return null;
        }
        queue.add(new State(p, b));
        trail.put(new State(p.copy(), b.copy()), state);
        return null;
    }

    private Boss parse() {
        int hp = Integer.parseInt(scanner.nextLine().split(": ")[1]);
        int atk = Integer.parseInt(scanner.nextLine().split(": ")[1]);
        return new Boss(hp, atk);
    }

    @Value
    private static class State {
        private Player player;
        private Boss boss;
    }

    @Getter
    @EqualsAndHashCode
    @AllArgsConstructor
    private static class Player {
        @Setter int hp;
        @Setter int mana;
        int shieldTurns;
        int poisonTurns;
        int rechargeTurns;
        int manaUsed;

        public Player copy() {
            return new Player(hp, mana, shieldTurns, poisonTurns, rechargeTurns, manaUsed);
        }

        public void heal(int amt) {
            hp += amt;
        }

        public void takeDamage(int dmg) {
            hp -= Math.max(dmg - (shieldTurns > 0 ? SHIELD.getDef() : 0), 1);
        }

        public void takeHardModeDmg() {
            hp -= 1;
        }

        public boolean isShieldActive() {
            return shieldTurns > 1;
        }

        public void activateShield() {
            shieldTurns = SHIELD.getTurns() + 1;
        }

        public boolean isPoisonActive() {
            return poisonTurns > 1;
        }

        public void activatePoison() {
            poisonTurns = POISON.getTurns() + 1;
        }

        public boolean isRechargeActive() {
            return rechargeTurns > 1;
        }

        public void activateRecharge() {
            rechargeTurns = RECHARGE.getTurns() + 1;
        }

        public void rechargeIfPossible() {
            if (rechargeTurns > 0) {
                mana += RECHARGE.getMana();
            }
        }

        public void useMana(int cost) {
            mana -= cost;
            manaUsed += cost;
        }

        public void turnEffects() {
            if (shieldTurns > 0) {
                shieldTurns--;
            }
            if (poisonTurns > 0) {
                poisonTurns--;
            }
            if (rechargeTurns > 0) {
                rechargeTurns--;
            }
        }

        private boolean hasMana(int cost) {
            return mana - cost >= 0;
        }

        public boolean dead() {
            return hp <= 0;
        }
    }

    @Getter
    @EqualsAndHashCode
    @AllArgsConstructor
    private static class Boss {
        @Setter private int hp;
        private int atk;

        public Boss copy() {
            return new Boss(hp, atk);
        }

        public void takeDamage(int dmg) {
            hp -= dmg;
        }

        public boolean dead() {
            return hp <= 0;
        }
    }

    @Value
    private static class Spell {
        SpellName name;
        int cost;
        int dmg;
        int def;
        int heal;
        int mana;
        int turns;
    }

    private enum SpellName {
        MAGIC_MISSILE, DRAIN, SHIELD, POISON, RECHARGE
    }

}
