package com.seniors.talents.config;

import com.seniors.talents.config.controller.LockItemControllerBuilder;
import com.seniors.talents.config.models.LockItem;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigField;
import dev.isxander.yacl3.config.v2.api.autogen.ListGroup;
import dev.isxander.yacl3.config.v2.api.autogen.OptionAccess;

import java.util.List;

public class LockItemListGroup implements ListGroup.ValueFactory<LockItem>, ListGroup.ControllerFactory<LockItem> {
    @Override
    public ControllerBuilder<LockItem> createController(ListGroup annotation, ConfigField<List<LockItem>> field, OptionAccess storage, Option<LockItem> option) {
        return LockItemControllerBuilder.create(option);
    }

    @Override
    public LockItem provideNewValue() {
        return new LockItem();
    }
}
