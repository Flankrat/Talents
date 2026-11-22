package com.seniors.talents.config.conditions;

import com.seniors.talents.config.models.TitleModel;
import com.seniors.talents.Talents;

import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;

public class AdvancementCondition extends ConditionImpl<Boolean> {

    public AdvancementCondition(){
        super("Advancement");
    }

    @Override
    public void ProcessVariable(String value, ServerPlayer serverPlayer) {
        Advancement advancement = Objects.requireNonNull(serverPlayer.getServer()).getAdvancements().getAdvancement(new ResourceLocation(value.replace("-", "/")));
        if (advancement == null){
            Talents.getLOGGER().error(">> Error! Advancement name {} not found!", value);
            setProcessedValue(false);
            return;
        }

        setProcessedValue(serverPlayer.getAdvancements().getOrStartProgress(advancement).isDone());
    }

    @Override
    public boolean MeetCondition(String value, TitleModel.EComparator comparator) {
        return getProcessedValue();
    }
}
