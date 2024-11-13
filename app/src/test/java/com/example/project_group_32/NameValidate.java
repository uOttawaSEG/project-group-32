package com.example.project_group_32;

import java.util.regex.Pattern;

public class NameValidate {

    boolean isNameValid(String name) {
        return Pattern.matches("\\b([A-ZÀ-ÿ][-,a-z. ']+)+", name);
    }
}
