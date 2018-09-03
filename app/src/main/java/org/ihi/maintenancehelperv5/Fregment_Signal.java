package org.ihi.maintenancehelperv5;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.content.Context.TELEPHONY_SERVICE;

public class Fregment_Signal extends Fragment {
    private static String TAG = "SignalStrength";
    private static String mTAG = "dBm";
    private TextView orignal = null;
    private SignalStrengthListener signalStrengthListener = null;
    private TelephonyManager tm = null;

    //private LineChart lineChart;
    private CustomLineChart lineChart = null;
    private int position = 0;
    private DynamicLineChartManager dynamicLineChartManager2 = null;
    private List<Integer> list = new ArrayList<>(); //数据集合
    private List<String> names = new ArrayList<>(); //折线名字集合
    private List<Integer> colour = new ArrayList<>();//折线颜色集合

    private ListView list_one = null;
    private MyAdapter mAdapter = null;
    private List<Data> mData = null;
    private View view;

    /*@Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (!isVisibleToUser) {
            orignal = null;
            signalStrengthListener = null;
            tm = null;
            lineChart = null;
            position = 0;
            dynamicLineChartManager2 = null;
            list = new ArrayList<>(); //数据集合
            names = new ArrayList<>(); //折线名字集合
            colour = new ArrayList<>();//折线颜色集合
            list_one = null;
            mAdapter = null;
            mData = null;
        }
        super.setUserVisibleHint(isVisibleToUser);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent) {
                parent.removeView(view);
            }
        } else {
            view = inflater.inflate(R.layout.fragment_signal, container, false);


            if (tm == null) {
                tm = (TelephonyManager) this.getContext().getSystemService(TELEPHONY_SERVICE);
                signalStrengthListener = new SignalStrengthListener();
                tm.listen(signalStrengthListener, SignalStrengthListener.LISTEN_SIGNAL_STRENGTHS);
            }

            if (orignal == null) {
                orignal = (TextView) view.findViewById(R.id.tv_Signal_Origine);
            }

            if (lineChart == null) {
                lineChart = (CustomLineChart) view.findViewById(R.id.LineChartMP_Signal);
                lineChart.setBgColor(getBg());
                //折线名字
                names.add("GSM");
                names.add("CDMA");
                names.add("EVDO");
                names.add("LTE");
                //折线颜色
                colour.add(Color.CYAN);
                colour.add(Color.BLUE);
                colour.add(Color.RED);
                colour.add(Color.BLACK);
                dynamicLineChartManager2 = new DynamicLineChartManager(lineChart, names, colour);
                dynamicLineChartManager2.setYAxis(-55, -125, 8);
            }

            if (list_one == null) {
                list_one = (ListView) view.findViewById(R.id.list_one);
                mData = new LinkedList<Data>();
                mAdapter = new MyAdapter((LinkedList<Data>) mData, getContext());
                list_one.setAdapter(mAdapter);
            }
        }
        return view;
    }

    private ArrayList<BgColor> getBg() {
        ArrayList<BgColor> bgList = new ArrayList<>();
        bgList.add(new BgColor(-125, -115, 0xFF696969));
        bgList.add(new BgColor(-115, -105, 0xFFA9A9A9));
        bgList.add(new BgColor(-105, -95, 0xFFFFD700));
        bgList.add(new BgColor(-95, -85, 0xFFFFFF00));
        bgList.add(new BgColor(-85, -75, 0xFFADFF2F));
        bgList.add(new BgColor(-75, -65, 0xFF00FF00));
        bgList.add(new BgColor(-65, -55, 0xFF228B22));
        return bgList;
    }

    private void addEntry_dynamic(int signalStrength_GSM, int signalStrength_CDMA, int signalStrength_EVDO, int signalStrength_LTE) {
        list.add(signalStrength_GSM);
        list.add(signalStrength_CDMA);
        list.add(signalStrength_EVDO);
        list.add(signalStrength_LTE);
        dynamicLineChartManager2.addEntry(list);
        list.clear();
    }

    private LineDataSet createSet(String setName, int color) {

        LineDataSet set = new LineDataSet(null, setName);
        set.setLineWidth(0.5f);
        set.setCircleRadius(2.0f);
        set.setColor(color);
        //set.setCircleColor(Color.rgb(240, 99, 99));
        //set.setHighLightColor(Color.rgb(190, 190, 190));
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setValueTextSize(10f);

        return set;
    }

    private class SignalStrengthListener extends PhoneStateListener {
        private int signalStrength_GSM = -140;
        private int signalStrength_CDMA = -140;
        private int signalStrength_LTE = -140;
        private int signalStrength_EVDO = -140;

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            /*<rssi>:
            0 -113 dBm or less
            1 -111 dBm
            2...30 -109... -53 dBm
            31 -51 dBm or greater
            99 not known or not detectable

            part[0] = "Signalstrength:"  _ignore this, it's just the title_
            parts[1] = GsmSignalStrength
            parts[2] = GsmBitErrorRate
            parts[3] = CdmaDbm
            parts[4] = CdmaEcio
            parts[5] = EvdoDbm
            parts[6] = EvdoEcio
            parts[7] = EvdoSnr
            parts[8] = LteSignalStrength
            parts[9] = LteRsrp
            parts[10] = LteRsrq
            parts[11] = LteRssnr
            parts[12] = LteCqi
            parts[13] = gsm|lte
            parts[14] = _not reall sure what this number is_*/

            if (10 > position) {
                String ltestr = signalStrength.toString();
                String[] parts = ltestr.split(" ");

                if (parts[1].equals("0")) {
                    signalStrength_GSM = -113;
                } else if (parts[1].equals("1")) {
                    signalStrength_GSM = -111;
                } else if (parts[1].equals("99")) {
                    signalStrength_GSM = -140;
                } else if (30 > Integer.parseInt(parts[1]) && Integer.parseInt(parts[1]) > 0) {
                    signalStrength_GSM = Integer.parseInt(parts[1]) * 2 - 113;
                } else if (-50 >= Integer.parseInt(parts[1]) && Integer.parseInt(parts[1]) >= -120) {
                    signalStrength_GSM = Integer.parseInt(parts[1]);
                } else {
                    //signalStrength_GSM = -117;
                }

                if (-50 >= Integer.parseInt(parts[5]) && Integer.parseInt(parts[5]) > -120) {
                    signalStrength_EVDO = Integer.parseInt(parts[5]);
                } else {
                    //signalStrength_EVDO = -118;
                }

                if (-50 >= Integer.parseInt(parts[3]) && Integer.parseInt(parts[3]) > -120) {
                    signalStrength_CDMA = Integer.parseInt(parts[3]);
                } else {
                    //signalStrength_CDMA = -119;
                }

                if (-50 >= Integer.parseInt(parts[8]) && Integer.parseInt(parts[8]) > -120) {
                    signalStrength_LTE = Integer.parseInt(parts[8]);
                } else {
                    //signalStrength_LTE = -120;
                }

                int dbm = -140;
                String type = "NONE";
                int cid = -1;
                mAdapter.clear();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    List<CellInfo> cellInfoList = tm.getAllCellInfo();
                    if (null != cellInfoList) {
                        for (CellInfo cellInfo : cellInfoList) {
                            if (cellInfo instanceof CellInfoGsm) {
                                CellSignalStrengthGsm cellSignalStrengthGsm = ((CellInfoGsm) cellInfo).getCellSignalStrength();
                                dbm = cellSignalStrengthGsm.getDbm();
                                type = "GSM";
                                cid = ((CellInfoGsm) cellInfo).getCellIdentity().getMcc();
                                if (signalStrength_GSM == -140) {
                                    signalStrength_GSM = dbm;
                                }
                            } else if (cellInfo instanceof CellInfoCdma) {
                                CellSignalStrengthCdma cellSignalStrengthCdma =
                                        ((CellInfoCdma) cellInfo).getCellSignalStrength();
                                dbm = cellSignalStrengthCdma.getDbm();
                                type = "CDMA";
                                cid = ((CellInfoCdma) cellInfo).getCellIdentity().getNetworkId();
                                if (signalStrength_CDMA == -140) {
                                    signalStrength_CDMA = dbm;
                                }
                            } else if (cellInfo instanceof CellInfoWcdma) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                                    CellSignalStrengthWcdma cellSignalStrengthWcdma =
                                            ((CellInfoWcdma) cellInfo).getCellSignalStrength();
                                    dbm = cellSignalStrengthWcdma.getDbm();
                                    type = "EVDO";
                                    cid = ((CellInfoWcdma) cellInfo).getCellIdentity().getMcc();
                                    if (signalStrength_EVDO == -140) {
                                        signalStrength_EVDO = dbm;
                                    }
                                }
                            } else if (cellInfo instanceof CellInfoLte) {
                                CellSignalStrengthLte cellSignalStrengthLte = ((CellInfoLte) cellInfo).getCellSignalStrength();
                                dbm = cellSignalStrengthLte.getDbm();
                                type = "LTE";
                                cid = ((CellInfoLte) cellInfo).getCellIdentity().getMcc();
                                if (signalStrength_LTE == -140) {
                                    signalStrength_LTE = dbm;
                                }
                            }

                            Resources resources = getContext().getResources();
                            Drawable drawable = resources.getDrawable(R.drawable.shape_label_black);
                            if (-55 >= dbm && dbm > -65) {
                                drawable = resources.getDrawable(R.drawable.shape_label_green);
                            }
                            if (-65 >= dbm && dbm > -75) {
                                drawable = resources.getDrawable(R.drawable.shape_label_lightgreen);
                            }
                            if (-75 >= dbm && dbm > -85) {
                                drawable = resources.getDrawable(R.drawable.shape_label_yellowgreen);
                            }
                            if (-85 >= dbm && dbm > -95) {
                                drawable = resources.getDrawable(R.drawable.shape_label_yellow);
                            }
                            if (-95 >= dbm && dbm > -105) {
                                drawable = resources.getDrawable(R.drawable.shape_label_darkyellow);
                            }
                            if (-105 >= dbm && dbm > -115) {
                                drawable = resources.getDrawable(R.drawable.shape_label_gray);
                            }
                            if (-115 >= dbm && dbm >= -125) {
                                drawable = resources.getDrawable(R.drawable.shape_label_darkyellow);
                            }


                            mAdapter.add(new Data("MCC:" + cid + "," + "类型：" + type + "," + "强度：" + dbm + "dBm", drawable));
                            //Log.i(TAG, cellInfo.toString());
                        }
                    } else {
                            /*List<NeighboringCellInfo> NeighboringCellInfoList = tm.getNeighboringCellInfo();
                            if (null != NeighboringCellInfoList) {
                                for (NeighboringCellInfo cellInfo : NeighboringCellInfoList) {
                                    int cid = cellInfo.getCid();
                                    // LAC:位置区域码。为了确定移动台的位置，每个GSM/PLMN的覆盖区都被划分成许多位置区，LAC则用于标识不同的位置区。
                                    int lac = cellInfo.getLac();
                                    // 获取邻居小区信号强度
                                    dbm = -131 + 2 * cellInfo.getRssi();

                                    mAdapter.add(new Data(R.mipmap.ic_news_gray, cellInfo.toString()));
                                    //Log.i(TAG, cellInfo.toString());
                                }
                            }*/
                    }
                }

                //Log.i(TAG, signalStrength.toString());

                Log.i(TAG, "GSM:" + signalStrength.getGsmSignalStrength() + "," + "CDMA:" + signalStrength.getCdmaDbm() + "," + "EVDO:" + signalStrength.getEvdoDbm() + "," + "Level:" + signalStrength.getLevel());

                orignal.setText(signalStrength.toString());

                //addEntry(signalStrength_GSM, signalStrength_CDMA, signalStrength_EVDO, signalStrength_LTE);
                addEntry_dynamic(signalStrength_GSM, signalStrength_CDMA, signalStrength_EVDO, signalStrength_LTE);

                position++;
            }

            super.onSignalStrengthsChanged(signalStrength);
        }
    }
}
