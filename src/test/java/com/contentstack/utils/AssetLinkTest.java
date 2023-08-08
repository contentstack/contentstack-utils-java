package com.contentstack.utils;

import com.contentstack.utils.render.DefaultOption;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class AssetLinkTest {

    private static JSONObject assetLink = null;

    @BeforeClass
    public static void startUtilTests() throws IOException {
        final String ASSERT_LINK = "src/test/resources/issue/jsonfile.json";
        assetLink = new ReadResource().readJson(ASSERT_LINK);
    }

    @Test
    public void testRenderFunction() {
        String[] keys = new String[1];
        keys[0] = "assetlink";
        Utils.jsonToHTML(assetLink, keys, new DefaultOption());
        System.out.println(assetLink);
        Assert.assertEquals("<img display-type=\"display\" asset-name=\"11.jpg\" asset-type=\"image/jpeg\" asset-uid=\"asset_uid_1\" width=\"25.16914749661705\" className=\"dsd\" id=\"sdf\" type=\"asset\" content-type-uid=\"sys_assets\" class-name=\"embedded-asset\" src=\"https://image.url/11.jpg\" />", assetLink.opt("assetlink").toString());
    }
}
