package org.schabi.newpipe.extractor.utils;

import java.net.MalformedURLException;
import java.net.URL;

public class DonationLinkHelper {
    public enum DonationService {
        NO_DONATION,
        PATREON,
        PAYPAL,
    }

    public enum AffiliateService {
        NO_AFFILIATE,
        AMAZON,
    }

    public static DonationService getDonationServiceByLink(String link) throws MalformedURLException {
        URL url = new URL(fixLink(link));
        switch (url.getHost()) {
            case "www.patreon.com":
            case "patreon.com":
                return DonationService.PATREON;
            case "www.paypal.me":
            case "paypal.me":
                return DonationService.PAYPAL;
            default:
                return DonationService.NO_DONATION;
        }
    }

    public static AffiliateService getAffiliateServiceByLink(String link) throws MalformedURLException {
        URL url = new URL(fixLink(link));
        switch (url.getHost()) {
            case "amzn.to": return AffiliateService.AMAZON;
            default: return AffiliateService.NO_AFFILIATE;
        }
    }

    private static String fixLink(String link) {
        return (link.startsWith("https://") || link.startsWith("http://"))
                ? link
                : "https://" + link;
    }

}
