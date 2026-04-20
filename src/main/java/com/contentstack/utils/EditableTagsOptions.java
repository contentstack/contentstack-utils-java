package com.contentstack.utils;

/**
 * Options for {@link Utils#addEditableTags(org.json.JSONObject, String, boolean, String, EditableTagsOptions)}.
 */
public final class EditableTagsOptions {

    private boolean useLowerCaseLocale = true;

    public EditableTagsOptions() {
    }

    /**
     * When {@code true} (default), the locale string is lowercased to match the JavaScript Utils default.
     *
     * @return whether locale is normalized to lowercase
     */
    public boolean isUseLowerCaseLocale() {
        return useLowerCaseLocale;
    }

    /**
     * @param useLowerCaseLocale if {@code true}, locale is lowercased; if {@code false}, locale is left as-is
     * @return this instance for chaining
     */
    public EditableTagsOptions setUseLowerCaseLocale(boolean useLowerCaseLocale) {
        this.useLowerCaseLocale = useLowerCaseLocale;
        return this;
    }
}
