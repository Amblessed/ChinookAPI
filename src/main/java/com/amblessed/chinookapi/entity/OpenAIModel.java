package com.amblessed.chinookapi.entity;



/*
 * @Project Name: ChinookAPI
 * @Author: Okechukwu Bright Onwumere
 * @Created: 02-Sep-25
 */

import lombok.Getter;

@Getter
public enum OpenAIModel {

    GPT_4O_MINI("gpt-4o-mini"),
    GPT_4O("gpt-4o"),
    GPT_3_5_TURBO("gpt-3.5-turbo");

    private final String modelName;

    OpenAIModel(String modelName) {
        this.modelName = modelName;
    }
}
