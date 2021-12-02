package com.mikemunhall.hbasedaotest.service;

import java.io.IOException;
import java.util.List;
import org.apache.hadoop.hbase.client.Result;

/**
 * HbaseService
 */
public interface HbaseService {

    /**
     * Interroger les données de la table entière
     */
    List<Result> scaner(String tablename);

    /**
     * Interroger un seul enregistrement basé sur rowKey
     */
    Result getRow(String tableName, String rowKey);

    /**
     * Faire correspondre les données selon regxKey
     */
    List<Result> getRegexRow(String tableName, String regxKey);

    /**
     * Selon les données de correspondance régulières de regxKey, retirez num
     */
    List<Result> getRegexRow(String tableName, String regxKey, int num);

    /**
     * Faire correspondre les données en fonction de la plage startKey - endKey
     */
    List<Result> getStartRowAndEndRow(String tableName, String startKey, String stopKey);

    /**
     * Pour la plage startKey - endKey, faire correspondre les données selon regKey
     */
    List<Result> getRegexRow(String tableName, String startKey, String stopKey, String regxKey);

    /**
     * Pour la plage startKey - endKey, extrayez num en fonction des données de correspondance regKey
     */
    List<Result> getRegexRow(String tableName, String startKey, String stopKey, String regxKey,
        int num);

    /**
     * ajout de données
     */
    void addData(String rowKey, String tableName, String[] column, String[] value);

    /**
     * Supprimer l'enregistrement
     */
    void delRecord(String tableName, String... rowKeys);

    /**
     * Modifier une donnée
     */
    void updateTable(String tableName, String rowKey, String familyName, String column[],
        String value[]) throws IOException;

    /**
     * Recherchez la dernière donnée ou effectuez une requête inversée
     */
    Result getNewRow(String tableName);

    /**
     * Trouvez régulièrement toutes les clés correspondantes
     */
    List<String> queryKeys(String tableName, String regxKey);

    /**
     * Augmenter la valeur du champ correspondant dans le tableau
     */
    long incrQualifier(String tableName, String cf, String rowKey, String column, long num);

    Result getNewRow(String tableName, String regxKey);
}
