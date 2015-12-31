package com.paramg.android.trydemo;

import org.json.JSONObject;

public interface JSONable
{
    /**
     * Must return a JSON-compliant representation of this object,
     * in the form of a JSONObject.
     *
     * @return The JSONObject representation.
     * @throws JSONException if any of the underlying JSONObject members throws.
     *                       Implementations may throw other unchecked exceptions.
     */
    JSONObject toJSONObject();

    /**
     * Must populate this object from the given JSON source.
     *
     * @param src The source JSON data.
     * @throws JSONException If any JSONObject members throw. Implementations
     *                       may optionally throw if the input object violates the expected structure.
     */
    void fromJSONObject(final JSONObject src);
}