/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cache;

/**
 *
 * @author MSI 15
 */
import Models.Voucher;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class VoucherCache {
    private static final ConcurrentHashMap<String, CacheEntry> cacheMap = new ConcurrentHashMap<>();

    public static List<Voucher> getCache(String key) {
        CacheEntry entry = cacheMap.get(key);
        if (entry != null && !entry.isExpired()) {
            return entry.getVouchers();
        }
        return null;
    }

    public static void setCache(String key, List<Voucher> vouchers, long ttlInMillis) {
        cacheMap.put(key, new CacheEntry(vouchers, System.currentTimeMillis() + ttlInMillis));
    }

    public static void clearCache(String key) {
        cacheMap.remove(key);
    }

    private static class CacheEntry {
        private final List<Voucher> vouchers;
        private final long expiryTime;

        CacheEntry(List<Voucher> vouchers, long expiryTime) {
            this.vouchers = vouchers;
            this.expiryTime = expiryTime;
        }

        public List<Voucher> getVouchers() {
            return vouchers;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }
}

