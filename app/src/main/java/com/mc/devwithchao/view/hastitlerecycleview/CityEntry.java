package com.mc.devwithchao.view.hastitlerecycleview;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/11/10.
 */

public class CityEntry {
    private static String[] city = new String[]{
            "重庆市", "廊坊市", "通化市", "七台河市", "蚌埠市", "安庆市", "宣城市", "福州市", "厦门市", "三明市", "龙岩市", "宁德市", "洛阳市", "驻马店市", "恩施土家族苗族自治州", "益阳市",
            "江门市", "阳江市", "梧州市", "海口市", "省直辖县级行政区划", "六盘水市", "曲靖市", "西安市", "兰州市", "定西市", "果洛藏族自治州", "吴忠市"
    };

    public static List<String> getCity() {
        return Arrays.asList(city);
    }
}
