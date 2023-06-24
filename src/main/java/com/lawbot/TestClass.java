package com.lawbot;

import org.apache.commons.validator.routines.UrlValidator;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class TestClass {

    public enum CharacterHighLightIntroPart2 {
        PROP1 {
            public String toString() { return " for giving us this lovely \"jobba\" "; }
        },
        PROP2 {
            public String toString() {
                return " for gracing LAW with the presence of ";
            }
        },
        PROP3 {
            public String toString() {
                return " for giving people the gift of ";
            }
        },
        PROP4 {
            public String toString() {
                return " for making this future champion right here ";
            }
        },
        PROP5 {
            public String toString() {
                return " for giving us the chance to wrassle with ";
            }
        }
    }

    private static final List<CharacterHighLightIntroPart2> FOLLOWUPTOINTRO =
            Collections.unmodifiableList(Arrays.asList(CharacterHighLightIntroPart2.values()));
    private static final int PROPSIZE2 = FOLLOWUPTOINTRO.size();
    private static final Random PROPRANDOM2 = new Random();

    public static CharacterHighLightIntroPart2 randomFollowUpMessage() {
        return FOLLOWUPTOINTRO.get(PROPRANDOM2.nextInt(PROPSIZE2));
    }

    public static void main(String[] args) throws IOException {
        UrlValidator urlValidator = new UrlValidator();

        System.out.println(urlValidator.isValid("https://i.ibb.co/tcN1Lg7/E73934-BE-77-F6-4-B48-91-EC-C3-E774-D40352.png"));

        URL url = new URL("https://i.ibb.co/tcN1Lg7/E73934-BE-77-F6-4-B48-91-EC-C3-E774-D40352.png");
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        huc.setRequestMethod("HEAD");

        System.out.println(huc.getResponseCode());

    }
    }





