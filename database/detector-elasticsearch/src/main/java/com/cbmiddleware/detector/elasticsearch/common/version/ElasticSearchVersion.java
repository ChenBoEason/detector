package com.cbmiddleware.detector.elasticsearch.common.version;

/**
 * @author Eason(bo.chenb)
 * @description es version
 * @email chenboeason@gmail.com
 * @date 2020-10-20
 **/
public enum ElasticSearchVersion {
    /**/
    VERSION_5,

    VERSION_6,

    VERSION_7,

    ;

    public static ElasticSearchVersion parse(String version) {

        for (ElasticSearchVersion searchVersion : values()) {
            if (searchVersion.name().equalsIgnoreCase(version)) {
                return searchVersion;
            }
        }

        return null;
    }
}
