package com.seniors.talents.integration;

import com.binaris.wizardry.api.content.event.SpellCastEvent;
import com.binaris.wizardry.api.content.spell.Spell;
import com.binaris.wizardry.core.event.EventPriorityOrder;
import com.binaris.wizardry.core.event.WizardryEventBus;
import com.seniors.talents.handler.HandlerLearnByDoing;
import com.seniors.talents.handler.HandlerLearnByDoingConfig;
import net.minecraft.server.level.ServerPlayer;

/**
 * Learn by doing: casting an Electroblob's Wizardry spell from a wand gives magic XP, by the spell's mana cost.
 * Wizardry has its own event bus (not Forge's), so the listeners are registered there. Only loaded when
 * Wizardry is installed (Talents checks for the "ebwizardry" mod first).
 * <ul>
 *   <li>Post fires once when a cast starts. Instant spells give their whole cost then.</li>
 *   <li>Tick fires every tick a continuous spell is held. Those give their cost (which Wizardry charges per second)
 *       once per 20 casting ticks, so holding a beam for 3 seconds counts 3 times.</li>
 * </ul>
 * Both listen at LOWEST priority and skip cancelled casts, and only wands count (not scrolls, NPCs or commands).
 */
public final class WizardryLearnByDoing {

    private WizardryLearnByDoing() {}

    public static void register() {
        WizardryEventBus.getInstance().register(SpellCastEvent.Post.class, WizardryLearnByDoing::onCast, EventPriorityOrder.LOWEST);
        WizardryEventBus.getInstance().register(SpellCastEvent.Tick.class, WizardryLearnByDoing::onCastTick, EventPriorityOrder.LOWEST);
    }

    /** A cast started: instant spells give magic XP for their full cost. */
    private static void onCast(SpellCastEvent.Post event) {
        Spell spell = event.getSpell();
        if (spell.isInstantCast()) give(event, spell);
    }

    /** A continuous spell is being held: give its per-second cost every 20 ticks. */
    private static void onCastTick(SpellCastEvent.Tick event) {
        Spell spell = event.getSpell();
        if (!spell.isInstantCast() && event.getTicksCasting() > 0 && event.getTicksCasting() % 20 == 0) give(event, spell);
    }

    private static void give(SpellCastEvent event, Spell spell) {
        if (event.isCanceled() || event.getSource() != SpellCastEvent.Sources.WAND) return;
        if (event.getCaster() instanceof ServerPlayer player)
            HandlerLearnByDoing.give(player, HandlerLearnByDoing.MAGIC, spell.getCost() * HandlerLearnByDoingConfig.HANDLER.instance().wandSpellMagic);
    }
}
