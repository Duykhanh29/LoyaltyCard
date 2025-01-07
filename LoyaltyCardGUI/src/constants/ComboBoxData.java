/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package constants;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Admin
 */
public class ComboBoxData {

    private Map<Integer, String> keyValueMap;

    public ComboBoxData() {
        keyValueMap = new LinkedHashMap<>();
        keyValueMap.put(1, "Tất cả");
        keyValueMap.put(2, "Có thể dùng");
        keyValueMap.put(3, "Không khả dụng");
    }

    public Map<Integer, String> getKeyValueMap() {
        return keyValueMap;
    }

    public Integer getKeyByValue(String value) {
        for (Map.Entry<Integer, String> entry : keyValueMap.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public String getValueByKey(Integer key) {
        return keyValueMap.getOrDefault(key, null);
    }
}
