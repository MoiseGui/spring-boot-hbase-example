package com.mikemunhall.hbasedaotest.service;

import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.TableCallback;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by quicksandzn@gmail.com on 2017/9/22.
 */
@Service
public class HbaseServiceImpl implements HbaseService {

    @Autowired
    private HbaseTemplate hbaseTemplate;
    private final String encoding = "utf-8";

    @Override
    public List<Result> scaner(final String tableName) {
        return hbaseTemplate.execute(tableName, new TableCallback<List<Result>>() {
            List<Result> list = new ArrayList<>();

            @Override
            public List<Result> doInTable(HTableInterface table) throws Throwable {
                Scan scan = new Scan();
                ResultScanner rs = table.getScanner(scan);
                for (Result result : rs) {
                    System.out.println("result.getValue() = " + Bytes.toString(result.getValue(Bytes.toBytes("Info"), Bytes.toBytes("companyName"))));
                    list.add(result);
                }
                return list;
            }

        });
    }

    @Override
    public Result getRow(final String tableName, final String rowKey) {
        return hbaseTemplate.execute(tableName, new TableCallback<Result>() {
            @Override
            public Result doInTable(HTableInterface table) throws Throwable {
                Get get = new Get(rowKey.getBytes(encoding));
                return table.get(get);
            }

        });
    }

    @Override
    public List<Result> getRegexRow(final String tableName, final String regxKey) {
        return hbaseTemplate.execute(tableName, new TableCallback<List<Result>>() {
            List<Result> list = new ArrayList<>();

            @Override
            public List<Result> doInTable(HTableInterface table) throws Throwable {
                RegexStringComparator rc = new RegexStringComparator(regxKey);
                RowFilter rowFilter = new RowFilter(CompareOp.EQUAL, rc);
                Scan scan = new Scan();
                scan.setFilter(rowFilter);
                ResultScanner rs = table.getScanner(scan);
                for (Result result : rs) {
                    list.add(result);
                }
                return list;
            }

        });
    }

    @Override
    public List<Result> getRegexRow(final String tableName, final String regxKey, final int num) {
        return hbaseTemplate.execute(tableName, new TableCallback<List<Result>>() {
            List<Result> list = new ArrayList<>();

            @Override
            public List<Result> doInTable(HTableInterface table) throws Throwable {
                FilterList fl = new FilterList(FilterList.Operator.MUST_PASS_ALL);
                RegexStringComparator rc = new RegexStringComparator(regxKey);
                RowFilter rf = new RowFilter(CompareOp.EQUAL, rc);
                if (num > 0) {// ?????????????????????
                    Filter filterNum = new PageFilter(num);// ??????????????????
                    fl.addFilter(filterNum);
                }
                // ??????????????????
                fl.addFilter(rf);
                Scan scan = new Scan();
                scan.setFilter(fl);// ???????????????????????????list
                ResultScanner rscanner = table.getScanner(scan);
                for (Result result : rscanner) {
                    list.add(result);
                }
                return list;
            }

        });
    }

    @Override
    public List<Result> getStartRowAndEndRow(final String tableName, final String startKey,
                                             final String stopKey) {
        return hbaseTemplate.execute(tableName, new TableCallback<List<Result>>() {
            List<Result> list = new ArrayList<>();

            @Override
            public List<Result> doInTable(HTableInterface table) throws Throwable {
                // ??????????????????
                Scan scan = new Scan();
                scan.setStartRow(startKey.getBytes(encoding));// ?????????key
                scan.setStopRow(stopKey.getBytes(encoding));// ?????????key
                ResultScanner rscanner = table.getScanner(scan);
                for (Result result : rscanner) {
                    list.add(result);
                }
                return list;
            }

        });
    }

    @Override
    public List<Result> getRegexRow(final String tableName, final String startKey,
                                    final String stopKey, final String regxKey) {
        return hbaseTemplate.execute(tableName, new TableCallback<List<Result>>() {
            List<Result> list = new ArrayList<>();

            @Override
            public List<Result> doInTable(HTableInterface table) throws Throwable {
                // ?????????????????????
                RegexStringComparator rc = new RegexStringComparator(regxKey);
                RowFilter rf = new RowFilter(CompareOp.EQUAL, rc);
                // ??????????????????
                Scan scan = new Scan();
                scan.setStartRow(startKey.getBytes(encoding));// ?????????key
                scan.setStopRow(stopKey.getBytes(encoding));// ?????????key
                scan.setFilter(rf);// ???????????????????????????list
                ResultScanner rscanner = table.getScanner(scan);
                for (Result result : rscanner) {
                    list.add(result);
                }
                return list;
            }
        });
    }

    @Override
    public List<Result> getRegexRow(final String tableName, final String startKey,
                                    final String stopKey, final String regxKey, final int num) {
        return hbaseTemplate.execute(tableName, new TableCallback<List<Result>>() {
            List<Result> list = new ArrayList<>();

            @Override
            public List<Result> doInTable(HTableInterface table) throws Throwable {
                FilterList fl = new FilterList(FilterList.Operator.MUST_PASS_ALL);
                // ?????????????????????
                RegexStringComparator rc = new RegexStringComparator(regxKey);
                RowFilter rf = new RowFilter(CompareOp.EQUAL, rc);
                if (num > 0) {// ?????????????????????
                    Filter filterNum = new PageFilter(num);// ??????????????????
                    fl.addFilter(filterNum);
                }
                // ??????????????????
                fl.addFilter(rf);
                // ??????????????????
                Scan scan = new Scan();
                scan.setStartRow(startKey.getBytes(encoding));// ?????????key
                scan.setStopRow(stopKey.getBytes(encoding));// ?????????key
                scan.setFilter(fl);// ???????????????????????????list
                ResultScanner rscanner = table.getScanner(scan);
                for (Result result : rscanner) {
                    list.add(result);
                }
                return list;
            }

        });
    }

    @Override
    public void addData(final String rowKey, final String tableName, final String[] column,
                        final String[] value) {
        hbaseTemplate.execute(tableName, new TableCallback<String>() {

            @Override
            public String doInTable(HTableInterface table) throws Throwable {

                Put put = new Put(Bytes.toBytes(rowKey));// ??????rowkey
                for (int j = 0; j < column.length; j++) {
                    put.add(Bytes.toBytes("cf1"), Bytes.toBytes(column[j]),
                            Bytes.toBytes(value[j]));
                }
                table.put(put);
                return "ok";
            }

        });
    }

    @Override
    public void delRecord(final String tableName, final String... rowKeys) {
        hbaseTemplate.execute(tableName, new TableCallback<String>() {

            @Override
            public String doInTable(HTableInterface table) throws Throwable {
                List<Delete> list = new ArrayList<>();
                for (String rowKey : rowKeys) {
                    Delete del = new Delete(Bytes.toBytes(rowKey));
                    list.add(del);
                }
                table.delete(list);
                return "ok";
            }

        });
    }

    @Override
    public void updateTable(final String tableName, final String rowKey, final String familyName,
                            final String[] column, final String[] value)
            throws IOException {
        hbaseTemplate.execute(tableName, new TableCallback<String>() {

            @Override
            public String doInTable(HTableInterface table) throws Throwable {
                Put put = new Put(Bytes.toBytes(rowKey));
                for (int j = 0; j < column.length; j++) {
                    put.add(Bytes.toBytes(familyName), Bytes.toBytes(column[j]),
                            Bytes.toBytes(value[j]));
                }
                table.put(put);
                return "ok";
            }

        });
    }

    @Override
    public Result getNewRow(final String tableName) {
        return hbaseTemplate.execute(tableName, new TableCallback<Result>() {

            @Override
            public Result doInTable(HTableInterface table) throws Throwable {
                Filter filterNum = new PageFilter(1);// ??????????????????
                Scan scan = new Scan();
                scan.setFilter(filterNum);
                scan.setReversed(true);
                ResultScanner scanner = table.getScanner(scan);
                return scanner.next();
            }

        });
    }

    @Override
    public Result getNewRow(final String tableName, final String regxKey) {
        return hbaseTemplate.execute(tableName, new TableCallback<Result>() {

            @Override
            public Result doInTable(HTableInterface table) throws Throwable {
                FilterList fl = new FilterList(FilterList.Operator.MUST_PASS_ALL);
                RegexStringComparator rc = new RegexStringComparator(regxKey);
                RowFilter rf = new RowFilter(CompareOp.EQUAL, rc);
                Filter filterNum = new PageFilter(1);// ??????????????????
                fl.addFilter(rf);
                fl.addFilter(filterNum);
                Scan scan = new Scan();
                scan.setFilter(fl);
                scan.setReversed(true);
                ResultScanner scanner = table.getScanner(scan);
                return scanner.next();
            }

        });
    }

    @Override
    public List<String> queryKeys(final String tableName, final String regxKey) {
        // TODO Auto-generated method stub
        return hbaseTemplate.execute(tableName, new TableCallback<List<String>>() {
            List<String> list = new ArrayList<>();

            @Override
            public List<String> doInTable(HTableInterface table) throws Throwable {
                PrefixFilter filter = new PrefixFilter(regxKey.getBytes(encoding));
                Scan scan = new Scan();
                scan.setFilter(filter);
                ResultScanner scanner = table.getScanner(scan);
                for (Result rs : scanner) {
                    list.add(new String(rs.getRow()));
                }
                return list;
            }

        });
    }

    @Override
    public long incrQualifier(final String tableName, final String cf, final String rowKey,
                              final String column, final long num) {
        // TODO Auto-generated method stub
        return hbaseTemplate.execute(tableName, new TableCallback<Long>() {
            @Override
            public Long doInTable(HTableInterface table) throws Throwable {
                long qualifie = table
                        .incrementColumnValue(rowKey.getBytes(encoding), cf.getBytes(encoding),
                                column.getBytes(encoding), num);
                return qualifie;
            }

        });
    }

}
