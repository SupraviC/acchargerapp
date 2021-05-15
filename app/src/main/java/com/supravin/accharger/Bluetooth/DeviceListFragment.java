package com.supravin.accharger.Bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.supravin.accharger.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class DeviceListFragment extends Fragment implements AbsListView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ArrayList <BluetoothDevice>deviceItemList=null;

    private OnFragmentInteractionListener mListener;
    private static BluetoothAdapter bTAdapter;
    ArrayList<String> arrayListpaired;
    BluetoothDevice bdDevice;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ArrayAdapter<String> adapter, mAdapter;


    private final BroadcastReceiver bReciever = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                Log.d("DEVICELIST", "Bluetooth device found\n");
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                // Create a new device item
//                DeviceItem newDevice = new DeviceItem(device.getName(), device.getAddress(), "false");
//                // Add it to our adapter
//                mAdapter.add(newDevice);
//                mAdapter.notifyDataSetChanged();
//            }

            Message msg = Message.obtain();
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
           //     Toast.makeText(context, "ACTION_FOUND", Toast.LENGTH_SHORT).show();

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                try
                {
                    //device.getClass().getMethod("setPairingConfirmation", boolean.class).invoke(device, true);
                    //device.getClass().getMethod("cancelPairingUserInput", boolean.class).invoke(device);
                }
                catch (Exception e) {
                    Log.i("Log", "Inside the exception: ");
                    e.printStackTrace();
                }

                if(deviceItemList.size()<1) // this checks if the size of bluetooth device is 0,then add the
                {                                           // device to the arraylist.
                    mAdapter.add(device.getName()+"\n"+device.getAddress());
                    deviceItemList.add(device);
                    mAdapter.notifyDataSetChanged();
                }
                else
                {
                    boolean flag = true;    // flag to indicate that particular device is already in the arlist or not
                    for(int i = 0; i<deviceItemList.size();i++)
                    {
                        if(device.getAddress().equals(deviceItemList.get(i).getAddress()))
                        {
                            flag = false;
                        }
                    }
                    if(flag == true)
                    {
                        mAdapter.add(device.getName()+"\n"+device.getAddress());
                        deviceItemList.add(device);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }


        }
    };



    // TODO: Rename and change types of parameters
    public static DeviceListFragment newInstance(BluetoothAdapter adapter) {
        DeviceListFragment fragment = new DeviceListFragment();
        bTAdapter = adapter;
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DeviceListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Log.d("DEVICELIST", "Super called for DeviceListFragment onCreate\n");
        deviceItemList = new ArrayList<BluetoothDevice>();
        arrayListpaired = new ArrayList<String>();
        adapter= new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayListpaired);
        mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice);

        getPairedDevices();
//        adapter.notifyDataSetChanged();





//        Set<BluetoothDevice> pairedDevices = bTAdapter.getBondedDevices();
//        if (pairedDevices.size() > 0) {
//            for (BluetoothDevice device : pairedDevices) {
//                DeviceItem newDevice= new DeviceItem(device.getName(),device.getAddress(),"false");
//                deviceItemList.add(newDevice);
//            }
//        }
//
//        // If there are no devices, add an item that states so. It will be handled in the view.
//        if(deviceItemList.size() == 0) {
//            deviceItemList.add(new DeviceItem("No Devices", "", "false"));
//        }
//
//        Log.d("DEVICELIST", "DeviceList populated\n");
//
   //     mAdapter = new DeviceListAdapter(getActivity(), deviceItemList, bTAdapter);
//
//        Log.d("DEVICELIST", "Adapter created\n");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deviceitem_list, container, false);
        ToggleButton scan = (ToggleButton) view.findViewById(R.id.scan);
        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);

        scan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                if (isChecked) {
                    mAdapter.clear();
                    getActivity().registerReceiver(bReciever, filter);
                    bTAdapter.startDiscovery();
                } else {
                    getActivity().unregisterReceiver(bReciever);
                    mAdapter.clear();
                    deviceItemList.clear();
                    bTAdapter.cancelDiscovery();

                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//        Log.d("DEVICELIST", "onItemClick position: " + position +
//                " id: " + id + " name: " + deviceItemList.get(position).getDeviceName() + "\n");
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
           // mListener.onFragmentInteraction(deviceItemList.get(position).getDeviceName());
       //     mListener.onFragmentInteraction(deviceItemList.get(position).getDeviceName());
//            bdDevice=deviceItemList.get(position);
//            if(deviceItemList.get(position).equals("0"))
//            {
//                Toast.makeText(getActivity(), "Click on Scan Button", Toast.LENGTH_SHORT).show();
//            }
            bdDevice=deviceItemList.get(position);
            Boolean isBonded = false;
            try {
                isBonded = createBond(bdDevice);
                if(isBonded)
                {
                    //arrayListpaired.add(bdDevice.getName()+"\n"+bdDevice.getAddress());
                    //adapter.notifyDataSetChanged();
                    getPairedDevices();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "Paired Successfully", Toast.LENGTH_SHORT).show();
                    Log.i("Log", "The bond is created:1 "+isBonded);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Failed to pair,please try again", Toast.LENGTH_SHORT).show();
            }//connect(bdDevice);
            Log.i("Log", "The bond is created: "+isBonded);


    }

    }



    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO Auto-generated method stub
        Toast.makeText(getActivity(), "LongClicked", Toast.LENGTH_SHORT).show();
        bdDevice=deviceItemList.get(position);
        Boolean isBonded = true;
        try {
            isBonded = removeBond(bdDevice);
            if(isBonded)
            {
                //arrayListpaired.add(bdDevice.getName()+"\n"+bdDevice.getAddress());
                //adapter.notifyDataSetChanged();
//                getPairedDevices();
                adapter.notifyDataSetChanged();
                Log.i("Log", "The unbond is created:1 "+isBonded);
                Toast.makeText(getActivity(), "Unpaired Successfully", Toast.LENGTH_SHORT).show();


                bTAdapter.startDiscovery();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Failed to unpair, please try again", Toast.LENGTH_SHORT).show();

        }//connect(bdDevice);
        Log.i("Log", "The unbond is created: "+isBonded);
        return false;
    }


    public boolean removeBond(BluetoothDevice btDevice)
            throws Exception
    {
        Class btClass = Class.forName("android.bluetooth.BluetoothDevice");
        Method removeBondMethod = btClass.getMethod("removeBond");
        Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }




private void getPairedDevices() {
        Set<BluetoothDevice> pairedDevice = bTAdapter.getBondedDevices();
        if(pairedDevice.size()>0)
        {
            for(BluetoothDevice device : pairedDevice)
            {
                arrayListpaired.add(device.getName()+"\n"+device.getAddress());
                deviceItemList.add(device);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private Boolean createBond(BluetoothDevice bdDevice) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class class1 = Class.forName("android.bluetooth.BluetoothDevice");
        Method createBondMethod = class1.getMethod("createBond");
        Boolean returnValue = (Boolean) createBondMethod.invoke(bdDevice);
        return returnValue.booleanValue();
    }

//    public boolean createBond(DeviceItem btDevice)
//            throws Exception
//    {
//
//    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
