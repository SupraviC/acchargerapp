/*

{                                        //if message is what we want
        isAlreadyonm = true;
        if (Power.isConnected(MaindisplayActivityMannual.this)) {
        String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread
        recDataString.append(readMessage);                                    //keep appending to string until ~
        int startOfLineIndex = recDataString.indexOf("#");
        int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
        //if (endOfLineIndex > 0 && endOfLineIndex < 80) {
        //if(endOfLineIndex > 0){
        Log.e("Indices", "readMessage" + readMessage + "\nstartOfLineIndex " + startOfLineIndex + "  endOfLineIndex " + endOfLineIndex + "\n recDataString" + recDataString);
        //if(startOfLineIndex == 0 && endOfLineIndex > 52 && endOfLineIndex < 82){
        if(endOfLineIndex > 0){
        // if(endOfLineIndex > 52){

        //if (!recDataString.toString().equals("")) {
        //if (startOfLineIndex == 0 && endOfLineIndex > 52 && endOfLineIndex < 82) {
        langSet();
        if (isTappedC1) {
        txt_touch1.setText("C1\n" + s24_CP1);
        } else {
        txt_touch1.setText("C1\n" + s21_CP1);

        }

        if (isTappedC2) {
        txt_touch2.setText("C2\n" + s24_CP2);
        } else {
        txt_touch2.setText("C2\n" + s21_CP2);

        }

        if (isTappedC3) {
        txt_touch3.setText("C3\n" + s24_CP3);
        } else {
        txt_touch3.setText("C3\n" + s21_CP3);

        }
        txt_etime_display1.setText(s14_CP1);
        txt_etime_display2.setText(s14_CP2);
        txt_etime_display3.setText(s14_CP3);
        // make sure there data before ~
        //get length of data received
        //txtStringLength.setText("String Length = " + String.valueOf(dataLength));
        if (recDataString.charAt(0) == '#')
        //if it starts with # we know it is what we are looking for
        {
        threadCurrentCount++;
        String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
        // txtString.setText("Data Received = " + dataInPrint);
        int dataLength = dataInPrint.length();
        if (dataLength < 80) {
        try {
        if (countidleCP1 > 400 && countidleCP2 > 400 && countidleCP3 > 400) {
        layout_detail.setVisibility(View.GONE);
        layout_main.setVisibility(View.GONE);
        layout_goback.setVisibility(View.VISIBLE);
        }
        int p1startV = recDataString.indexOf("a") + 1;
        int p1startC = recDataString.indexOf("b") + 1;
        int p1startM = recDataString.indexOf("d") + 1;
        int p2startV = recDataString.indexOf("f") + 1;
        int p2startC = recDataString.indexOf("i") + 1;
        int p2startM = recDataString.indexOf("j") + 1;
        int p3startV = recDataString.indexOf("l") + 1;
        int p3startC = recDataString.indexOf("m") + 1;
        int p3startM = recDataString.indexOf("n") + 1;

        int p1endV = recDataString.indexOf("b");
        int p1endC = recDataString.indexOf("d");
        int p1endM = recDataString.indexOf("e");
        int p2endV = recDataString.indexOf("i");
        int p2endC = recDataString.indexOf("j");
        int p2endM = recDataString.indexOf("k");
        int p3endV = recDataString.indexOf("m");
        int p3endC = recDataString.indexOf("n");
        int p3endM = recDataString.indexOf("%");

        String p1OnOff = recDataString.substring(recDataString.indexOf("#") + 1, recDataString.indexOf("#") + 2);
        String p1plugin = recDataString.substring(recDataString.indexOf("#") + 2, recDataString.indexOf("#") + 3);
        String p1fault = recDataString.substring(3, recDataString.indexOf("a"));
        String p1voltage = recDataString.substring(p1startV, p1endV);
        String p1current = recDataString.substring(p1startC, p1endC);
        String p1meter = recDataString.substring(p1startM, p1endM);
        globalP1meter = p1meter;
        String p2OnOff = recDataString.substring(recDataString.indexOf("e") + 1, recDataString.indexOf("e") + 2);
        String p2plugin = recDataString.substring(recDataString.indexOf("e") + 2, recDataString.indexOf("e") + 3);
        String p2fault = recDataString.substring(recDataString.indexOf("e") + 3, recDataString.indexOf("f"));
        String p2voltage = recDataString.substring(p2startV, p2endV);
        String p2current = recDataString.substring(p2startC, p2endC);
        String p2meter = recDataString.substring(p2startM, p2endM);
        globalP2meter = p2meter;
        String p3OnOff = recDataString.substring(recDataString.indexOf("k") + 1, recDataString.indexOf("k") + 2);
        String p3plugin = recDataString.substring(recDataString.indexOf("k") + 2, recDataString.indexOf("k") + 3);
        String p3fault = recDataString.substring(recDataString.indexOf("k") + 3, recDataString.indexOf("l"));
        String p3voltage = recDataString.substring(p3startV, p3endV);
        String p3current = recDataString.substring(p3startC, p3endC);
        String p3meter = recDataString.substring(p3startM, p3endM);
        globalP3meter = p3meter;
        String emergency = recDataString.substring(p3endM + 1, p3endM + 2);
        voltage_detailCP1 = p1voltage;
        current_detailCP1 = p1current;
        power_detailCP1 = p1meter;
        voltage_detailCP2 = p2voltage;
        current_detailCP2 = p2current;
        power_detailCP2 = p2meter;
        voltage_detailCP3 = p3voltage;
        current_detailCP3 = p3current;
        power_detailCP3 = p3meter;
        mFirebaseInstance.getReference(cid + "-CP1M").setValue(tv_status1.getText().toString());
        mFirebaseInstance.getReference(cid + "-CP2M").setValue(tv_status2.getText().toString());
        mFirebaseInstance.getReference(cid + "-CP3M").setValue(tv_status3.getText().toString());
        mFirebaseInstance.getReference(cid + "-CP1-VoltageM").setValue(p1voltage);
        mFirebaseInstance.getReference(cid + "-CP2-VoltageM").setValue(p2voltage);
        mFirebaseInstance.getReference(cid + "-CP3-VoltageM").setValue(p3voltage);
        mFirebaseInstance.getReference(cid + "-CP1-CurrentM").setValue(p1current);
        mFirebaseInstance.getReference(cid + "-CP2-CurrentM").setValue(p2current);
        mFirebaseInstance.getReference(cid + "-CP3-CurrentM").setValue(p3current);
        mFirebaseInstance.getReference(cid + "-CP1-PowerM").setValue(p1meter);
        mFirebaseInstance.getReference(cid + "-CP2-PowerM").setValue(p2meter);
        mFirebaseInstance.getReference(cid + "-CP3-PowerM").setValue(p3meter);

        mFirebaseInstance.getReference(cid + "-CP1-AmountM").setValue(txt_c1round_off_mr.getText().toString());
        mFirebaseInstance.getReference(cid + "-CP2-AmountM").setValue(txt_c2round_off_mr.getText().toString());
        mFirebaseInstance.getReference(cid + "-CP3-AmountM").setValue(txt_c3round_off_mr.getText().toString());


        //   txt_c1round_off_mr

        Log.e("SAVEDMONEY", sPsavemoneyafterpfCP1.getMeterReadingCP1RO());
        Log.e("PFA4FLAGC2", sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1());
        Log.e("PFA4FLAGC2", sPispowerfailafter4mrCP2.getispowerfailafter4mrCP2());
        Log.e("PFA4FLAGC3", sPispowerfailafter4mrCP3.getispowerfailafter4mrCP3());
        Log.e("==============", "========================================");

        Log.e("sPisPoweFLAGC1", sPisPoweFail.getisPowerFailCP1());
        Log.e("sPisPoweFLAGC2", sPisPoweFail.getisPowerFailCP2());
        Log.e("sPisPoweFLAGC3", sPisPoweFail.getisPowerFailCP3());

        //---------------------------------------------------------------------


        //----------------------------------------roundrobin instruction goes here.....
        if (isAvailableC1 && isAvailableC2 && isAvailableC3) {
        if (countC1 == 0 && countC2 == 0 && countC3 == 0) {
        txt_maininstruction.setText("All Connectors available");

        } else if (countC1 < countC2 && countC1 < countC3) {
        txt_maininstruction.setText("Please use connector C1");

        } else if (countC2 < countC1 && countC2 < countC3) {
        txt_maininstruction.setText("Please use connector C2");

        } else if (countC3 < countC1 && countC3 < countC2) {
        txt_maininstruction.setText("Please use connector C3");

        }
        //
        else if (countC1 < countC2 && countC2 < countC3) {
        txt_maininstruction.setText(" Use connector c1 or C2");

        } else if (countC2 < countC1 && countC1 < countC3) {
        txt_maininstruction.setText(" Use connector c1 or C2");

        } else if (countC3 < countC1 && countC1 < countC2) {
        txt_maininstruction.setText(" Use connector c1 or C3");

        } else if (countC1 < countC3 && countC3 < countC2) {
        txt_maininstruction.setText(" Use connector c1 or C3");

        } else if (countC3 < countC2 && countC2 < countC1) {
        txt_maininstruction.setText(" Use connector c2 or C3");

        } else if (countC2 < countC3 && countC3 < countC1) {
        txt_maininstruction.setText(" Use connector c2 or C3");

        } else {
        txt_maininstruction.setText("All Connectors available");

        }
        } else if (!isAvailableC1 && isAvailableC2 && isAvailableC3) {
        if (countC2 > countC3) {
        txt_maininstruction.setText("Please use connector C3");

        } else if (countC2 < countC3) {
        txt_maininstruction.setText("Please use connector C2");

        } else {
        txt_maininstruction.setText(" Use connector c2 or C3");

        }
        } else if (isAvailableC1 && !isAvailableC2 && isAvailableC3) {
        if (countC1 > countC3) {
        txt_maininstruction.setText("Please use connector C3");

        } else if (countC1 < countC3) {
        txt_maininstruction.setText("Please use connector C1");

        } else {
        txt_maininstruction.setText(" Use connector c1 or C3");

        }

        } else if (!isAvailableC1 && !isAvailableC2 && isAvailableC3) {
        txt_maininstruction.setText("Please use connector C3");


        } else if (isAvailableC1 && isAvailableC2 && !isAvailableC3) {
        if (countC1 > countC2) {
        txt_maininstruction.setText("Please use connector C2");

        } else if (countC1 < countC2) {
        txt_maininstruction.setText("Please use connector C1");

        } else {
        txt_maininstruction.setText(" Use connector c1 or C2");

        }
        } else if (!isAvailableC1 && isAvailableC2 && !isAvailableC3) {
        txt_maininstruction.setText("Please use connector C2");

        } else if (isAvailableC1 && !isAvailableC2 && !isAvailableC3) {
        txt_maininstruction.setText("Please use connector C1");

        } else if (!isAvailableC1 && !isAvailableC2 && !isAvailableC3) {
        txt_maininstruction.setText("Connectors not available");

        }
        Log.e("QisComefromPFUVOV", "" + isComefromPFUVOV);
        Log.e("Qp1fault", p1fault);
        Log.e("Qis99Comming", "" + is99Comming);
        Log.e("QCounterOutside", "" + counterPFUVOV);

        if (isComefromPFUVOV && p1fault.equals("99")) {
        Log.e("QCounterinsidebefore", "" + counterPFUVOV);
        is99Comming = false;
        counterPFUVOV++;
        if (counterPFUVOV > 12) {
        isComefromPFUVOV = false;
        is99Comming = true;
        counterPFUVOV = 0;
        Log.e("QCounterinside", "" + counterPFUVOV);

        }
        Log.e("QCounterinsideafter", "" + counterPFUVOV);


        }

        //----------------------------------
        if (is99Comming) {

        if (p1OnOff.equals("0")) {
        if (sPisPoweFail.getisPowerFailCP1().equals("t") && sPisPluggedin.getisPluggedinCP1().equals("t")) {
        toggleBtn.performClick();
        } else {
        flag = false;
        setToggleBtn();
        }
        } else {
        flag = true;
        setToggleBtn();
        }

        if (p2OnOff.equals("0")) {

        if (sPisPoweFail.getisPowerFailCP2().equals("t") && sPisPluggedin.getisPluggedinCP2().equals("t")) {
        btn_p2onff.performClick();
        } else {
        p2flag = false;
        setP2Btn();
        }

        } else {
        p2flag = true;
        setP2Btn();
        }

        if (p3OnOff.equals("0")) {
        if (sPisPoweFail.getisPowerFailCP3().equals("t") && sPisPluggedin.getisPluggedinCP3().equals("t")) {
        btn_p3onff.performClick();
        } else {
        p3flag = false;
        setP3Btn();
        }
        } else {
        p3flag = true;
        setP3Btn();
        }
        if (emergency.equals("1")) {
        tv_status1.setText(s2_CP1);
        tv_status2.setText(s2_CP2);
        tv_status3.setText(s2_CP3);
        status_detailCP1 = s2_CP1;
        status_detailCP2 = s2_CP2;
        status_detailCP3 = s2_CP3;

        } else {

        //---------------------------------------------------------------------------------------------------------

        if (p1OnOff.equals("0") && p1plugin.equals("0") && p1fault.equals("03")) {
        tv_status1.setText(s3_CP1);
        status_detailCP1 = s3_CP1;
        }
        if (p2OnOff.equals("0") && p2plugin.equals("0") && p2fault.equals("03")) {
        tv_status2.setText(s3_CP2);
        status_detailCP2 = s3_CP2;
        }
        if (p3OnOff.equals("0") && p3plugin.equals("0") && p3fault.equals("03")) {
        tv_status3.setText(s3_CP3);
        status_detailCP3 = s3_CP3;
        }
        //fault--------------------------------------------------------------------
        if (p1fault.equals("05")) {
        isComefromPFUVOV = true;
        Float cp1v = Float.parseFloat(p1voltage);
        if (cp1v < 260 && cp1v > 195) {

        } else {
*/
/*
                                                        toggleBtn.setCardBackgroundColor(Color.parseColor(red));
                                                        toggleBtn.startAnimation(anim);*//*

                                                        */
/*Log.e("Powerfailure", "OutsideC1");

                                                        if (sPisPluggedin.getisPluggedinCP1().equals("t")) {
                                                            sPisPoweFail.setisPowerFailCP1("t");
                                                            sPTimeReadingCP1.setTimeReadingCP1(MillisCP1);
                                                            isStillOnCP1 = true;
                                                            Log.e("Pluggedintrue", "PluggedintrueC1");

                                                        }*//*


        if (cp1v > 263) {
        tv_status1.setText(s13_CP1);
        status_detailCP1 = s13_CP1;
        Log.e("Powerfailure", "OverVoltageC1");

        } else if (cp1v < 195 && cp1v > 150) {
        tv_status1.setText(s12_CP1);
        status_detailCP1 = s12_CP1;
        Log.e("Powerfailure", "UndevoltageC1");

        } else {
        tv_status1.setText(s6_CP1);
        status_detailCP1 = s6_CP1;
        Log.e("Powerfailure", "PowerfailureC1");


        }
        }

        }
        if (p2fault.equals("05")) {
        isComefromPFUVOV = true;

        Float cp2v = Float.parseFloat(p2voltage);
        if (cp2v < 260 && cp2v > 195) {

        } else {

*/
/*
                                                        btn_p2onff.setCardBackgroundColor(Color.parseColor(red));
                                                        btn_p2onff.startAnimation(anim);
                                                        Log.e("Powerfailure", "OutsideC2");*//*


                                                       */
/* if (sPisPluggedin.getisPluggedinCP2().equals("t")) {
                                                            sPisPoweFail.setisPowerFailCP2("t");
                                                            sPTimeReadingCP2.setTimeReadingCP2(MillisCP2);
                                                            isStillOnCP2 = true;
                                                            Log.e("Pluggedintrue", "PluggedinTrueC2");

                                                        }*//*


        if (cp2v > 263) {
        tv_status2.setText(s13_CP2);
        status_detailCP2 = s13_CP2;
        Log.e("Powerfailure", "OverVoltageC2");


        } else if (cp2v < 195 && cp2v > 150) {
        tv_status2.setText(s12_CP2);
        status_detailCP2 = s12_CP2;

        Log.e("Powerfailure", "UnderVoltageC2");

        } else {
        tv_status2.setText(s6_CP2);
        status_detailCP2 = s6_CP2;

        Log.e("Powerfailure", "PowerfailureC2");


        }
        }


        }
        if (p3fault.equals("05")) {
        isComefromPFUVOV = true;

        Float cp3v = Float.parseFloat(p3voltage);
        if (cp3v < 260 && cp3v > 195) {

        } else {


                                                        */
/*btn_p3onff.setCardBackgroundColor(Color.parseColor(red));
                                                        Log.e("Powerfailure", "PowerfailureC3");

                                                        btn_p3onff.startAnimation(anim);
                                                        if (sPisPluggedin.getisPluggedinCP3().equals("t")) {
                                                            sPisPoweFail.setisPowerFailCP3("t");
                                                            sPTimeReadingCP3.setTimeReadingCP3(MillisCP3);
                                                            isStillOnCP3 = true;
                                                            Log.e("Pluggedintrue", "PluggeinC3");

                                                        }*//*


        if (cp3v > 263) {
        tv_status3.setText(s13_CP3);
        status_detailCP3 = s13_CP3;
        Log.e("Powerfailure", "OverVoltageC3");


        } else if (cp3v < 195 && cp3v > 150) {
        tv_status3.setText(s12_CP3);
        status_detailCP3 = s12_CP3;
        Log.e("Powerfailure", "UnderVoltageC3");


        } else {
        tv_status3.setText(s6_CP3);
        status_detailCP3 = s6_CP3;
        Log.e("Powerfailure", "PowerfailureC3");
        }
        }

        }
        //--------------Earth Fault
        if (p1fault.equals("10")) {
        tv_status1.setText(s4_CP1);
        status_detailCP1 = s4_CP1;

        }
        if (p2fault.equals("10")) {
        tv_status2.setText(s4_CP2);
        status_detailCP2 = s4_CP2;

        }
        if (p3fault.equals("10")) {
        tv_status3.setText(s4_CP3);
        status_detailCP3 = s4_CP3;

        }
        //---------------TEMP HAZARD
        if (p1fault.equals("07")) {
        tv_status1.setText(s5_CP1);
        status_detailCP1 = s5_CP1;


        }
        if (p2fault.equals("07")) {
        tv_status2.setText(s5_CP2);
        status_detailCP2 = s5_CP2;

        }
        if (p3fault.equals("07")) {
        tv_status3.setText(s5_CP3);
        status_detailCP3 = s5_CP3;

        }


//-----------------------------------------------------------CP1 Status-----------------------------------------------------------------------------------------------------------------------------
        if (p1OnOff.equals("0") && p1plugin.equals("0") && p1fault.equals("99")) {

        if (sPisPluggedin.getisPluggedinCP1().equals("t")) {

        if (plugedoutCountcp1 >= 30) {
        sPisPluggedin.setisPluggedinCP1("f");

        } else {
        displayPlugoutCountC1 = 0;
        tv_status1.setText(s7_CP1);
        isAvailableC1 = true;

        txt_c1round_off_mr.setText(rsPOCP1);

        rsCP1 = "₹ 00.00";
        txt_c1diff.setText(etime_detailCP1);

        status_detailCP1 = s7_CP1;
        unit_detailCP1 = unit_detailCP1;
        etime_detailCP1 = etime_detailCP1;
        // sPispowerfailafter4mrCP1.setispowerfailafter4mrCP1("f");

        plugedinCountcp1 = 0;
        plugedoutCountcp1++;
        if (isResumedAftercp1) {
        sPisPoweFail.setisPowerFailCP1("f");
        sPispowerfailafter4mrCP1.setispowerfailafter4mrCP1("f");

        }
        }

        } else {
        countidleCP1++;
        if (isPleaseWaitC1) {
        tv_status1.setText("Please Wait...");
        status_detailCP1 = "Please Wait...";
        toggleBtn.setVisibility(View.INVISIBLE);

        } else {
        tv_status1.setText(s8_CP1);
        status_detailCP1 = s8_CP1;

        }


        if (isStopC1) {
        //   countC1 = 0;
        isAvailableC1 = true;

        isStartC1 = true;
        isStopC1 = false;
        }

        txt_c1round_off_mr.setText("₹ 00.00");
        rsCP1 = "₹ 00.00";
        rsPOCP1 = "₹ 00.00";
        //------------
        float mrv = 0;
        mrsPOCP1 = "" + mrv;
        //-----------
        txt_c1diff.setText("00:00:00");
        unit_detailCP1 = "00.00";
        etime_detailCP1 = "00:00:00";
        plugedinCountcp1 = 0;
        toggleBtn.clearAnimation();
        //overlay1
        if (overlayCounti > 10) {
        isPleaseWaitC1 = false;
        relativeLayout1st.setVisibility(View.VISIBLE);
        }
        overlayCounti++;
        }

        }
        if (p1OnOff.equals("1") && p1plugin.equals("0") && p1fault.equals("99")) {


        countidleCP1 = 0;
        plugedinCountcp1 = 0;
        plugedoutCountcp1 = 0;
        tv_status1.setText(s1_CP1);
        isTappedC1 = false;
        isPleaseWaitC1 = false;

        if (isStartC1) {
        isStartC1 = false;
        countC1++;
        isStopC1 = true;
        isAvailableC1 = false;
        }
        txt_c1round_off_mr.setText("₹ 00.00");
        rsCP1 = "₹ 00.00";
        txt_c1diff.setText("00:00:00");
        status_detailCP1 = s1_CP1;
        unit_detailCP1 = "00.00";
        etime_detailCP1 = "00:00:00";
        isStillOnCP1 = false;
        relativeLayout1st.setVisibility(View.GONE);
        toggleBtn.setVisibility(View.VISIBLE);

        if (displayPlugoutCountC1 == 12) {
        if (sPisPoweFail.getisPowerFailCP1().equals("t") && sPisPluggedin.getisPluggedinCP1().equals("t")) {
        toggleBtn.performClick();
        sPisPoweFail.setisPowerFailCP1("f");
        sPisPluggedin.setisPluggedinCP1("f");
        tv_status1.setText(s7_CP1);

        status_detailCP1 = s7_CP1;
        // sPispowerfailaf ter4mrCP1.setispowerfailafter4mrCP1("f");

        //-----------
        sPisPoweFail.setisPowerFailCP1("f");
        // sPTimeReadingCP1.setTimeReadingCP1(MillisCP1);
        }
        }
        displayPlugoutCountC1++;
        //-----------------------------------------===============================================
                                                       */
/* sPisPoweFail.setisPowerFailCP1("t");
                                                        sPTimeReadingCP1.setTimeReadingCP1(MillisCP1);*//*



        }
        if (p1OnOff.equals("1") && p1plugin.equals("1") && p1fault.equals("99")) {

        countidleCP1 = 0;
        if (sPisPoweFail.getisPowerFailCP1().equals("t")) {
        //---------Status:resuming after power fail_______________
        if (plugedinCountcp1 >= 10) {
        //time reading______
        MillisCP1 = (SystemClock.uptimeMillis() - StartTimeCP1);

        HoursCP1 = (int) (MillisCP1 / (1000 * 60 * 60));
        MinutesCP1 = (int) (MillisCP1 / (1000 * 60)) % 60;
        SecondsCP1 = (int) (MillisCP1 / 1000) % 60;
        String time_readingCP1 = "" + String.format("%02d", HoursCP1) + ":"
        + String.format("%02d", MinutesCP1) + ":"
        + String.format("%02d", SecondsCP1);
        //-------------------------meter reading
        float prev_mrCP1 = Float.parseFloat(sPmeterReadingCP1.getMeterReadingCP1());
        float current_mrCP1 = 0.0f;
        Log.e("Metervaluesbefore", "prev_mrCP1" + prev_mrCP1 + "current_mrCP1 " + current_mrCP1);
        try {


        if (sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1().equals("t")) {
        current_mrCP1 = (Float.parseFloat(sPmeterReadingCP1RO.getMeterReadingCP1RO()) * 4) + Float.parseFloat(p1meter);
        //current_mrCP1 = Float.parseFloat("4.00")+Float.parseFloat(p1meter);
        Log.e("current_mrCP1insideadd", "" + current_mrCP1 + " b ");
        }
        if (sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1().equals("f")) {
        current_mrCP1 = Float.parseFloat(p1meter);
        int b = (int) current_mrCP1;
        b = b / 4;
        sPmeterReadingCP1RO.setMeterReadingCP1RO("" + b);
        Log.e("current_mrCP1inside", "" + current_mrCP1 + " b " + b);

        }
        Log.e("Metervaluesafter", "prev_mrCP1" + prev_mrCP1 + "current_mrCP1 " + current_mrCP1);

        float meterreadingdiff = current_mrCP1 - prev_mrCP1;
        float meterreadingdiffP = 0.0f;
        meterreadingdiffP = (current_mrCP1 - prev_mrCP1) * erate;

        Log.e("SAVEDMONEYINSIDE", "" + meterreadingdiffP);
        Log.e("SAVEDMONEYFLAGINSIDE", sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1());
        String meterreadingCP1 = String.format("%.2f", meterreadingdiff);
        String meterreadingCP1P = String.format("%.2f", meterreadingdiffP);

        //------------------
        tv_status1.setText(s10_CP1);
        // isSavedTimeC1 = false;
        if (meterreadingdiffP > 0 && meterreadingdiffP < 200) {
        txt_c1round_off_mr.setText("₹ " + meterreadingCP1P);
        rsCP1 = "₹ " + meterreadingCP1P;
        rsPOCP1 = "₹ " + meterreadingCP1P;
        //------------
        float mrv = meterreadingdiffP;
        mrsPOCP1 = "" + mrv;
        //-----------
        }
        txt_c1diff.setText(time_readingCP1);
        status_detailCP1 = s10_CP1;
        unit_detailCP1 = meterreadingCP1;
        etime_detailCP1 = time_readingCP1;
        sPisPoweFail.setisPowerFailCP1("f");
        relativeLayout1st.setVisibility(View.GONE);
        } catch (NumberFormatException ex) {

        }

        } else if (plugedinCountcp1 == 0) {
        //Time reading_________________

        StartTimeCP1 = SystemClock.uptimeMillis() + (-sPTimeReadingCP1.getTimeReadingCP1());
        //-------------------------meter reading
        float prev_mrCP1 = Float.parseFloat(sPmeterReadingCP1.getMeterReadingCP1());
        float current_mrCP1 = 0.0f;
        try {


        Log.e("Metervaluesbefore", "prev_mrCP1" + prev_mrCP1 + "current_mrCP1 " + current_mrCP1);
        if (sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1().equals("t")) {
        current_mrCP1 = (Float.parseFloat(sPmeterReadingCP1RO.getMeterReadingCP1RO()) * 4) + Float.parseFloat(p1meter);
        //current_mrCP1 = Float.parseFloat("4.00")+Float.parseFloat(p1meter);
        Log.e("current_mrCP1insideadd", "" + current_mrCP1 + " b ");
        }
        if (sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1().equals("f")) {
        current_mrCP1 = Float.parseFloat(p1meter);
        int b = (int) current_mrCP1;
        b = b / 4;
        sPmeterReadingCP1RO.setMeterReadingCP1RO("" + b);
        Log.e("current_mrCP1inside", "" + current_mrCP1 + " b " + b);

        }
        Log.e("Metervaluesafter", "prev_mrCP1" + prev_mrCP1 + "current_mrCP1 " + current_mrCP1);

        float meterreadingdiff = current_mrCP1 - prev_mrCP1;
        float meterreadingdiffP = 0.0f;
        meterreadingdiffP = (current_mrCP1 - prev_mrCP1) * erate;

        Log.e("SAVEDMONEYINSIDE", "" + meterreadingdiffP);
        Log.e("SAVEDMONEYFLAGINSIDE", sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1());
        String meterreadingCP1 = String.format("%.2f", meterreadingdiff);
        String meterreadingCP1P = String.format("%.2f", meterreadingdiffP);
        //--------------------
        tv_status1.setText(s9_CP1);
        if (meterreadingdiffP > 0 && meterreadingdiffP < 200) {
        txt_c1round_off_mr.setText("₹ " + meterreadingCP1P);
        rsCP1 = "₹ " + meterreadingCP1P;
        rsPOCP1 = "₹ " + meterreadingCP1P;
        //------------
        float mrv = meterreadingdiffP;
        mrsPOCP1 = "" + mrv;
        //-----------
        }
        txt_c1diff.setText("00:00:00");
        status_detailCP1 = s9_CP1;
        unit_detailCP1 = meterreadingCP1;
        etime_detailCP1 = "00:00:00";
        plugedinCountcp1++;
        sPisPluggedin.setisPluggedinCP1("t");
        isResumedAftercp1 = true;
        toggleBtn.startAnimation(anim);
        relativeLayout1st.setVisibility(View.GONE);
        isAvailableC1 = false;

        } catch (NumberFormatException ex) {

        }


        } else {
        //time reading______
        MillisCP1 = (SystemClock.uptimeMillis() - StartTimeCP1);

        HoursCP1 = (int) (MillisCP1 / (1000 * 60 * 60));
        MinutesCP1 = (int) (MillisCP1 / (1000 * 60)) % 60;
        SecondsCP1 = (int) (MillisCP1 / 1000) % 60;
        String time_readingCP1 = "" + String.format("%02d", HoursCP1) + ":"
        + String.format("%02d", MinutesCP1) + ":"
        + String.format("%02d", SecondsCP1);

        //-------------------------meter reading
        float prev_mrCP1 = Float.parseFloat(sPmeterReadingCP1.getMeterReadingCP1());
        float current_mrCP1 = 0.0f;
        try {
        Log.e("Metervaluesbefore", "prev_mrCP1" + prev_mrCP1 + "current_mrCP1 " + current_mrCP1);
        if (sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1().equals("t")) {
        current_mrCP1 = (Float.parseFloat(sPmeterReadingCP1RO.getMeterReadingCP1RO()) * 4) + Float.parseFloat(p1meter);
        //current_mrCP1 = Float.parseFloat("4.00")+Float.parseFloat(p1meter);
        Log.e("current_mrCP1insideadd", "" + current_mrCP1 + " b ");
        }
        if (sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1().equals("f")) {
        current_mrCP1 = Float.parseFloat(p1meter);
        int b = (int) current_mrCP1;
        b = b / 4;
        sPmeterReadingCP1RO.setMeterReadingCP1RO("" + b);
        Log.e("current_mrCP1inside", "" + current_mrCP1 + " b " + b);

        }
        Log.e("Metervaluesafter", "prev_mrCP1" + prev_mrCP1 + "current_mrCP1 " + current_mrCP1);

        float meterreadingdiff = current_mrCP1 - prev_mrCP1;
        float meterreadingdiffP = 0.0f;
        meterreadingdiffP = (current_mrCP1 - prev_mrCP1) * erate;

        Log.e("SAVEDMONEYINSIDE", "" + meterreadingdiffP);
        Log.e("SAVEDMONEYFLAGINSIDE", sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1());
        String meterreadingCP1 = String.format("%.2f", meterreadingdiff);
        String meterreadingCP1P = String.format("%.2f", meterreadingdiffP);
        //--------------------

        tv_status1.setText(s9_CP1);
        isAvailableC1 = false;

        if (meterreadingdiffP > 0 && meterreadingdiffP < 200) {
        txt_c1round_off_mr.setText("₹ " + meterreadingCP1P);
        rsCP1 = "₹ " + meterreadingCP1P;
        rsPOCP1 = "₹ " + meterreadingCP1P;
        //------------
        float mrv = meterreadingdiffP;
        mrsPOCP1 = "" + mrv;
        //-----------
        }
        txt_c1diff.setText(time_readingCP1);
        status_detailCP1 = s9_CP1;
        unit_detailCP1 = meterreadingCP1;
        etime_detailCP1 = time_readingCP1;
        plugedinCountcp1++;
        sPisPluggedin.setisPluggedinCP1("t");
        isResumedAftercp1 = true;
        relativeLayout1st.setVisibility(View.GONE);
        } catch (NumberFormatException ex) {

        }

        }


        } else {
        //--------------- Status:vehicle pugged in_________
        if (plugedinCountcp1 >= 10) {
        //time reading______
        MillisCP1 = (SystemClock.uptimeMillis() - StartTimeCP1);

        HoursCP1 = (int) (MillisCP1 / (1000 * 60 * 60));
        MinutesCP1 = (int) (MillisCP1 / (1000 * 60)) % 60;
        SecondsCP1 = (int) (MillisCP1 / 1000) % 60;
        String time_readingCP1 = "" + String.format("%02d", HoursCP1) + ":"
        + String.format("%02d", MinutesCP1) + ":"
        + String.format("%02d", SecondsCP1);

        //-------------------------meter reading
        float prev_mrCP1 = Float.parseFloat(sPmeterReadingCP1.getMeterReadingCP1());
        float current_mrCP1 = 0.0f;
        Log.e("Metervaluesbefore", "prev_mrCP1" + prev_mrCP1 + "current_mrCP1 " + current_mrCP1);
        try {


        if (sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1().equals("t")) {
        current_mrCP1 = (Float.parseFloat(sPmeterReadingCP1RO.getMeterReadingCP1RO()) * 4) + Float.parseFloat(p1meter);
        //current_mrCP1 = Float.parseFloat("4.00")+Float.parseFloat(p1meter);
        Log.e("current_mrCP1insideadd", "" + current_mrCP1 + " b ");
        }
        if (sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1().equals("f")) {
        current_mrCP1 = Float.parseFloat(p1meter);
        int b = (int) current_mrCP1;
        b = b / 4;
        sPmeterReadingCP1RO.setMeterReadingCP1RO("" + b);
        Log.e("current_mrCP1inside", "" + current_mrCP1 + " b " + b);

        }

        Log.e("Metervaluesafter", "prev_mrCP1" + prev_mrCP1 + "current_mrCP1 " + current_mrCP1);

        float meterreadingdiff = current_mrCP1 - prev_mrCP1;
        float meterreadingdiffP = 0.0f;
        meterreadingdiffP = (current_mrCP1 - prev_mrCP1) * erate;

        Log.e("SAVEDMONEYINSIDE", "" + meterreadingdiffP);
        Log.e("SAVEDMONEYFLAGINSIDE", sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1());
        String meterreadingCP1 = String.format("%.2f", meterreadingdiff);
        String meterreadingCP1P = String.format("%.2f", meterreadingdiffP);

        tv_status1.setText(s10_CP1);
        //isSavedTimeC1 = false;
        if (meterreadingdiffP > 0 && meterreadingdiffP < 200) {
        txt_c1round_off_mr.setText("₹ " + meterreadingCP1P);
        rsCP1 = "₹ " + meterreadingCP1P;
        rsPOCP1 = "₹ " + meterreadingCP1P;
        //------------
        float mrv = meterreadingdiffP;
        mrsPOCP1 = "" + mrv;
        //-----------
        }
        txt_c1diff.setText(time_readingCP1);
        status_detailCP1 = s10_CP1;
        unit_detailCP1 = meterreadingCP1;
        etime_detailCP1 = time_readingCP1;
        // sPisPoweFail.setisPowerFailCP1("t");
        relativeLayout1st.setVisibility(View.GONE);
        } catch (NumberFormatException ex) {

        }


        } else if (plugedinCountcp1 == 0) {
        tv_status1.setText(s11_CP1);
        isAvailableC1 = false;

        txt_c1round_off_mr.setText("₹ 00.00");
        rsCP1 = "₹ 00.00";
        txt_c1diff.setText("00:00:00");
        status_detailCP1 = s11_CP1;
        unit_detailCP1 = "00.00";
        etime_detailCP1 = "00:00:00";
        sPisPluggedin.setisPluggedinCP1("t");
        //-----------------meter reading
        sPispowerfailafter4mrCP1.setispowerfailafter4mrCP1("f");
        //-------------
        plugedinCountcp1++;

        //meter reading-------------------------
        sPmeterReadingCP1.setMeterReadingCP1(p1meter);
        //Time reading_________________
        StartTimeCP1 = SystemClock.uptimeMillis() + (-NewBeginMillsCP1);  //--> Start Time
        toggleBtn.startAnimation(anim);

        } else {
        //time reading______
        MillisCP1 = (SystemClock.uptimeMillis() - StartTimeCP1);

        HoursCP1 = (int) (MillisCP1 / (1000 * 60 * 60));
        MinutesCP1 = (int) (MillisCP1 / (1000 * 60)) % 60;
        SecondsCP1 = (int) (MillisCP1 / 1000) % 60;
        String time_readingCP1 = "" + String.format("%02d", HoursCP1) + ":"
        + String.format("%02d", MinutesCP1) + ":"
        + String.format("%02d", SecondsCP1);


        //-----------meter Reading
        float prev_mrCP1 = Float.parseFloat(sPmeterReadingCP1.getMeterReadingCP1());
        float current_mrCP1 = 0.0f;
        try {


        Log.e("Metervaluesbefore", "prev_mrCP1" + prev_mrCP1 + "current_mrCP1 " + current_mrCP1);
        if (sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1().equals("t")) {
        current_mrCP1 = (Float.parseFloat(sPmeterReadingCP1RO.getMeterReadingCP1RO()) * 4) + Float.parseFloat(p1meter);
        //current_mrCP1 = Float.parseFloat("4.00")+Float.parseFloat(p1meter);
        Log.e("current_mrCP1insideadd", "" + current_mrCP1 + " b ");
        }
        if (sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1().equals("f")) {
        current_mrCP1 = Float.parseFloat(p1meter);
        int b = (int) current_mrCP1;
        b = b / 4;
        sPmeterReadingCP1RO.setMeterReadingCP1RO("" + b);
        Log.e("current_mrCP1inside", "" + current_mrCP1 + " b " + b);

        }
        Log.e("Metervaluesafter", "prev_mrCP1" + prev_mrCP1 + "current_mrCP1 " + current_mrCP1);

        float meterreadingdiff = current_mrCP1 - prev_mrCP1;
        float meterreadingdiffP = 0.0f;
        meterreadingdiffP = (current_mrCP1 - prev_mrCP1) * erate;

        Log.e("SAVEDMONEYFLAGINSIDE", sPispowerfailafter4mrCP1.getispowerfailafter4mrCP1());
        String meterreadingCP1 = String.format("%.2f", meterreadingdiff);
        String meterreadingCP1P = String.format("%.2f", meterreadingdiffP);

        //--------------------
        tv_status1.setText(s11_CP1);
        isAvailableC1 = false;

        if (meterreadingdiffP > 0 && meterreadingdiffP < 200) {
        txt_c1round_off_mr.setText("₹ " + meterreadingCP1P);
        rsCP1 = "₹ " + meterreadingCP1P;
        rsPOCP1 = "₹ " + meterreadingCP1P;
        //------------
        float mrv = meterreadingdiffP;
        mrsPOCP1 = "" + mrv;
        //-----------
        }
        txt_c1diff.setText(time_readingCP1);
        status_detailCP1 = s11_CP1;
        unit_detailCP1 = meterreadingCP1;
        etime_detailCP1 = time_readingCP1;
        sPisPluggedin.setisPluggedinCP1("t");
        //---------------------
        sPispowerfailafter4mrCP1.setispowerfailafter4mrCP1("f");
        //-----------
        //sPisPoweFail.setisPowerFailCP1("t");
        // sPTimeReadingCP1.setTimeReadingCP1(MillisCP1);------------------TTTTTTTTTTTTTTTT
        //-------------------------------
        plugedinCountcp1++;
        } catch (NumberFormatException ex) {

        }
        }

        }

        }


//=======================================================================================================================================================================================================================================================================================

        //-----------------------------------------------------------CP2 Status-----------------------------------------------------------------------------------------------------------------------------
        if (p2OnOff.equals("0") && p2plugin.equals("0") && p2fault.equals("99")) {

        if (sPisPluggedin.getisPluggedinCP2().equals("t")) {

        if (plugedoutCountcp2 >= 30) {
        sPisPluggedin.setisPluggedinCP2("f");

        } else {
        displayPlugoutCountC2 = 0;
        tv_status2.setText(s7_CP2);
        isAvailableC2 = true;

        txt_c2round_off_mr.setText(rsPOCP2);

        rsCP2 = "₹ 00.00";
        txt_c2diff.setText(etime_detailCP2);

        status_detailCP2 = s7_CP2;
        unit_detailCP2 = unit_detailCP2;
        etime_detailCP2 = etime_detailCP2;
        // sPispowerfailafter4mrCP1.setispowerfailafter4mrCP1("f");

        plugedinCountcp2 = 0;
        plugedoutCountcp2++;
        if (isResumedAftercp2) {
        sPisPoweFail.setisPowerFailCP2("f");
        sPispowerfailafter4mrCP2.setispowerfailafter4mrCP2("f");

        }
        }

        } else {
        countidleCP2++;
        if (isPleaseWaitC2) {
        tv_status2.setText("Please Wait...");
        status_detailCP2 = "Please Wait...";
        btn_p2onff.setVisibility(View.INVISIBLE);

        } else {
        tv_status2.setText(s8_CP2);
        status_detailCP2 = s8_CP2;

        }


        if (isStopC2) {
        //   countC1 = 0;
        isAvailableC2 = true;

        isStartC2 = true;
        isStopC2 = false;
        }

        txt_c2round_off_mr.setText("₹ 00.00");
        rsCP2 = "₹ 00.00";
        rsPOCP2 = "₹ 00.00";
        //------------
        float mrv = 0;
        mrsPOCP2 = "" + mrv;
        //-----------
        txt_c2diff.setText("00:00:00");
        unit_detailCP2 = "00.00";
        etime_detailCP2 = "00:00:00";
        plugedinCountcp2 = 0;
        btn_p2onff.clearAnimation();
        //overlay1
        if (overlayCountii > 10) {
        isPleaseWaitC2 = false;
        relativeLayout2nd.setVisibility(View.VISIBLE);
        }
        overlayCountii++;
        }

        }
        if (p2OnOff.equals("1") && p2plugin.equals("0") && p2fault.equals("99")) {

        countidleCP2 = 0;
        plugedinCountcp2 = 0;
        plugedoutCountcp2 = 0;
        tv_status2.setText(s1_CP2);
        isTappedC2 = false;
        isPleaseWaitC2 = false;

        if (isStartC2) {
        isStartC2 = false;
        countC2++;
        isStopC2 = true;
        isAvailableC2 = false;
        }
        txt_c2round_off_mr.setText("₹ 00.00");
        rsCP2 = "₹ 00.00";
        txt_c2diff.setText("00:00:00");
        status_detailCP2 = s1_CP2;
        unit_detailCP2 = "00.00";
        etime_detailCP2 = "00:00:00";
        isStillOnCP2 = false;
        relativeLayout2nd.setVisibility(View.GONE);
        btn_p2onff.setVisibility(View.VISIBLE);

        if (displayPlugoutCountC2 == 12) {
        if (sPisPoweFail.getisPowerFailCP2().equals("t") && sPisPluggedin.getisPluggedinCP2().equals("t")) {
        btn_p2onff.performClick();
        sPisPoweFail.setisPowerFailCP2("f");
        sPisPluggedin.setisPluggedinCP2("f");
        tv_status2.setText(s7_CP2);

        status_detailCP2 = s7_CP2;
        // sPispowerfailafter4mrCP1.setispowerfailafter4mrCP1("f");

        //-----------
        sPisPoweFail.setisPowerFailCP2("f");
        // sPTimeReadingCP1.setTimeReadingCP1(MillisCP1);
        }
        }
        displayPlugoutCountC2++;

        //------------------------------=====================================
                                                       */
/* sPisPoweFail.setisPowerFailCP2("t");
                                                        sPTimeReadingCP2.setTimeReadingCP2(MillisCP2);*//*



        }
        if (p2OnOff.equals("1") && p2plugin.equals("1") && p2fault.equals("99")) {

        countidleCP2 = 0;
        if (sPisPoweFail.getisPowerFailCP2().equals("t")) {
        //---------Status:resuming after power fail_______________
        if (plugedinCountcp2 >= 10) {
        //time reading______
        MillisCP2 = (SystemClock.uptimeMillis() - StartTimeCP2);

        HoursCP2 = (int) (MillisCP2 / (1000 * 60 * 60));
        MinutesCP2 = (int) (MillisCP2 / (1000 * 60)) % 60;
        SecondsCP2 = (int) (MillisCP2 / 1000) % 60;
        String time_readingCP2 = "" + String.format("%02d", HoursCP2) + ":"
        + String.format("%02d", MinutesCP2) + ":"
        + String.format("%02d", SecondsCP2);
        //-------------------------meter reading
        float prev_mrCP2 = Float.parseFloat(sPmeterReadingCP2.getMeterReadingCP2());
        float current_mrCP2 = 0.0f;
        Log.e("Metervaluesbefore", "prev_mrCP2" + prev_mrCP2 + "current_mrCP2 " + current_mrCP2);
        try {


        if (sPispowerfailafter4mrCP2.getispowerfailafter4mrCP2().equals("t")) {
        current_mrCP2 = (Float.parseFloat(sPmeterReadingCP2RO.getMeterReadingCP2RO()) * 4) + Float.parseFloat(p2meter);
        //current_mrCP1 = Float.parseFloat("4.00")+Float.parseFloat(p1meter);
        Log.e("current_mrCP2insideadd", "" + current_mrCP2 + " b ");
        }
        if (sPispowerfailafter4mrCP2.getispowerfailafter4mrCP2().equals("f")) {
        current_mrCP2 = Float.parseFloat(p2meter);
        int b = (int) current_mrCP2;
        b = b / 4;
        sPmeterReadingCP2RO.setMeterReadingCP2RO("" + b);
        Log.e("current_mrCP2inside", "" + current_mrCP2 + " b " + b);

        }
        Log.e("Metervaluesafter", "prev_mrCP2" + prev_mrCP2 + "current_mrCP2 " + current_mrCP2);

        float meterreadingdiff2 = current_mrCP2 - prev_mrCP2;
        float meterreadingdiffP2 = 0.0f;
        meterreadingdiffP2 = (current_mrCP2 - prev_mrCP2) * erate;

        Log.e("SAVEDMONEYINSIDE", "" + meterreadingdiffP2);
        Log.e("SAVEDMONEYFLAGINSIDE", sPispowerfailafter4mrCP2.getispowerfailafter4mrCP2());
        String meterreadingCP2 = String.format("%.2f", meterreadingdiff2);
        String meterreadingCP2P = String.format("%.2f", meterreadingdiffP2);

        //------------------
        tv_status2.setText(s10_CP2);
        // isSavedTimeC2 = false;
        if (meterreadingdiffP2 > 0 && meterreadingdiffP2 < 200) {
        txt_c2round_off_mr.setText("₹ " + meterreadingCP2P);
        rsCP2 = "₹ " + meterreadingCP2P;
        rsPOCP2 = "₹ " + meterreadingCP2P;
        //------------
        float mrv = meterreadingdiffP2;
        mrsPOCP2 = "" + mrv;
        //-----------
        }
        txt_c2diff.setText(time_readingCP2);
        status_detailCP2 = s10_CP2;
        unit_detailCP2 = meterreadingCP2;
        etime_detailCP2 = time_readingCP2;
        sPisPoweFail.setisPowerFailCP2("f");
        relativeLayout2nd.setVisibility(View.GONE);
        } catch (NumberFormatException ex) {

        }

        } else if (plugedinCountcp2 == 0) {
        //Time reading_________________

        StartTimeCP2 = SystemClock.uptimeMillis() + (-sPTimeReadingCP2.getTimeReadingCP2());
        //-------------------------meter reading
        float prev_mrCP2 = Float.parseFloat(sPmeterReadingCP2.getMeterReadingCP2());
        float current_mrCP2 = 0.0f;
        try {


        Log.e("Metervaluesbefore", "prev_mrCP2" + prev_mrCP2 + "current_mrCP2 " + current_mrCP2);
        if (sPispowerfailafter4mrCP2.getispowerfailafter4mrCP2().equals("t")) {
        current_mrCP2 = (Float.parseFloat(sPmeterReadingCP2RO.getMeterReadingCP2RO()) * 4) + Float.parseFloat(p2meter);
        //current_mrCP1 = Float.parseFloat("4.00")+Float.parseFloat(p1meter);
        Log.e("current_mrCP2insideadd", "" + current_mrCP2 + " b ");
        }
        if (sPispowerfailafter4mrCP2.getispowerfailafter4mrCP2().equals("f")) {
        current_mrCP2 = Float.parseFloat(p2meter);
        int b = (int) current_mrCP2;
        b = b / 4;
        sPmeterReadingCP2RO.setMeterReadingCP2RO("" + b);
        Log.e("current_mrCP2inside", "" + current_mrCP2 + " b " + b);

        }
        Log.e("Metervaluesafter", "prev_mrCP2" + prev_mrCP2 + "current_mrCP2 " + current_mrCP2);

        float meterreadingdiff2 = current_mrCP2 - prev_mrCP2;
        float meterreadingdiffP2 = 0.0f;
        meterreadingdiffP2 = (current_mrCP2 - prev_mrCP2) * erate;

        Log.e("SAVEDMONEYINSIDE", "" + meterreadingdiffP2);
        Log.e("SAVEDMONEYFLAGINSIDE", sPispowerfailafter4mrCP2.getispowerfailafter4mrCP2());
        String meterreadingCP2 = String.format("%.2f", meterreadingdiff2);
        String meterreadingCP2P = String.format("%.2f", meterreadingdiffP2);
        //--------------------
        tv_status2.setText(s9_CP2);
        if (meterreadingdiffP2 > 0 && meterreadingdiffP2 < 200) {
        txt_c2round_off_mr.setText("₹ " + meterreadingCP2P);
        rsCP2 = "₹ " + meterreadingCP2P;
        rsPOCP2 = "₹ " + meterreadingCP2P;
        //------------
        float mrv = meterreadingdiffP2;
        mrsPOCP2 = "" + mrv;
        //-----------
        }
        txt_c2diff.setText("00:00:00");
        status_detailCP2 = s9_CP2;
        unit_detailCP2 = meterreadingCP2;
        etime_detailCP2 = "00:00:00";
        plugedinCountcp2++;
        sPisPluggedin.setisPluggedinCP2("t");
        isResumedAftercp2 = true;
        btn_p2onff.startAnimation(anim);
        relativeLayout2nd.setVisibility(View.GONE);
        isAvailableC2 = false;

        } catch (NumberFormatException ex) {

        }


        } else {
        //time reading______
        MillisCP2 = (SystemClock.uptimeMillis() - StartTimeCP2);

        HoursCP2 = (int) (MillisCP2 / (1000 * 60 * 60));
        MinutesCP2 = (int) (MillisCP2 / (1000 * 60)) % 60;
        SecondsCP2 = (int) (MillisCP2 / 1000) % 60;
        String time_readingCP2 = "" + String.format("%02d", HoursCP2) + ":"
        + String.format("%02d", MinutesCP2) + ":"
        + String.format("%02d", SecondsCP2);

        //-------------------------meter reading
        float prev_mrCP2 = Float.parseFloat(sPmeterReadingCP2.getMeterReadingCP2());
        float current_mrCP2 = 0.0f;
        try {
        Log.e("Metervaluesbefore", "prev_mrCP2" + prev_mrCP2 + "current_mrCP2 " + current_mrCP2);
        if (sPispowerfailafter4mrCP2.getispowerfailafter4mrCP2().equals("t")) {
        current_mrCP2 = (Float.parseFloat(sPmeterReadingCP2RO.getMeterReadingCP2RO()) * 4) + Float.parseFloat(p2meter);
        //current_mrCP1 = Float.parseFloat("4.00")+Float.parseFloat(p1meter);
        Log.e("current_mrCP2insideadd", "" + current_mrCP2 + " b ");
        }
        if (sPispowerfailafter4mrCP2.getispowerfailafter4mrCP2().equals("f")) {
        current_mrCP2 = Float.parseFloat(p2meter);
        int b = (int) current_mrCP2;
        b = b / 4;
        sPmeterReadingCP2RO.setMeterReadingCP2RO("" + b);
        Log.e("current_mrCP2inside", "" + current_mrCP2 + " b " + b);

        }
        Log.e("Metervaluesafter", "prev_mrCP2" + prev_mrCP2 + "current_mrCP2 " + current_mrCP2);

        float meterreadingdiff2 = current_mrCP2 - prev_mrCP2;
        float meterreadingdiffP2 = 0.0f;
        meterreadingdiffP2 = (current_mrCP2 - prev_mrCP2) * erate;

        Log.e("SAVEDMONEYINSIDE", "" + meterreadingdiffP2);
        Log.e("SAVEDMONEYFLAGINSIDE", sPispowerfailafter4mrCP2.getispowerfailafter4mrCP2());
        String meterreadingCP2 = String.format("%.2f", meterreadingdiff2);
        String meterreadingCP2P = String.format("%.2f", meterreadingdiffP2);
        //--------------------

        tv_status2.setText(s9_CP2);
        isAvailableC2 = false;

        if (meterreadingdiffP2 > 0 && meterreadingdiffP2 < 200) {
        txt_c2round_off_mr.setText("₹ " + meterreadingCP2P);
        rsCP2 = "₹ " + meterreadingCP2P;
        rsPOCP2 = "₹ " + meterreadingCP2P;
        //------------
        float mrv = meterreadingdiffP2;
        mrsPOCP2 = "" + mrv;
        //-----------
        }
        txt_c2diff.setText(time_readingCP2);
        status_detailCP2 = s9_CP2;
        unit_detailCP2 = meterreadingCP2;
        etime_detailCP2 = time_readingCP2;
        plugedinCountcp2++;
        sPisPluggedin.setisPluggedinCP2("t");
        isResumedAftercp2 = true;
        relativeLayout2nd.setVisibility(View.GONE);
        } catch (NumberFormatException ex) {

        }

        }


        } else {
        //--------------- Status:vehicle pugged in_________
        if (plugedinCountcp2 >= 10) {
        //time reading______
        MillisCP2 = (SystemClock.uptimeMillis() - StartTimeCP2);

        HoursCP2 = (int) (MillisCP2 / (1000 * 60 * 60));
        MinutesCP2 = (int) (MillisCP2 / (1000 * 60)) % 60;
        SecondsCP2 = (int) (MillisCP2 / 1000) % 60;
        String time_readingCP2 = "" + String.format("%02d", HoursCP2) + ":"
        + String.format("%02d", MinutesCP2) + ":"
        + String.format("%02d", SecondsCP2);

        //-------------------------meter reading
        float prev_mrCP2 = Float.parseFloat(sPmeterReadingCP2.getMeterReadingCP2());
        float current_mrCP2 = 0.0f;
        Log.e("Metervaluesbefore", "prev_mrCP2" + prev_mrCP2 + "current_mrCP2 " + current_mrCP2);
        try {


        if (sPispowerfailafter4mrCP2.getispowerfailafter4mrCP2().equals("t")) {
        current_mrCP2 = (Float.parseFloat(sPmeterReadingCP2RO.getMeterReadingCP2RO()) * 4) + Float.parseFloat(p2meter);
        //current_mrCP1 = Float.parseFloat("4.00")+Float.parseFloat(p1meter);
        Log.e("current_mrCP2insideadd", "" + current_mrCP2 + " b ");
        }
        if (sPispowerfailafter4mrCP2.getispowerfailafter4mrCP2().equals("f")) {
        current_mrCP2 = Float.parseFloat(p2meter);
        int b = (int) current_mrCP2;
        b = b / 4;
        sPmeterReadingCP2RO.setMeterReadingCP2RO("" + b);
        Log.e("current_mrCP2inside", "" + current_mrCP2 + " b " + b);

        }

        Log.e("Metervaluesafter", "prev_mrCP2" + prev_mrCP2 + "current_mrCP2 " + current_mrCP2);

        float meterreadingdiff2 = current_mrCP2 - prev_mrCP2;
        float meterreadingdiffP2 = 0.0f;
        meterreadingdiffP2 = (current_mrCP2 - prev_mrCP2) * erate;

        Log.e("SAVEDMONEYINSIDE", "" + meterreadingdiffP2);
        Log.e("SAVEDMONEYFLAGINSIDE", sPispowerfailafter4mrCP2.getispowerfailafter4mrCP2());
        String meterreadingCP2 = String.format("%.2f", meterreadingdiff2);
        String meterreadingCP2P = String.format("%.2f", meterreadingdiffP2);

        tv_status2.setText(s10_CP2);
        //  isSavedTimeC2 = false;
        if (meterreadingdiffP2 > 0 && meterreadingdiffP2 < 200) {
        txt_c2round_off_mr.setText("₹ " + meterreadingCP2P);
        rsCP2 = "₹ " + meterreadingCP2P;
        rsPOCP2 = "₹ " + meterreadingCP2P;
        //------------
        float mrv = meterreadingdiffP2;
        mrsPOCP1 = "" + mrv;
        //-----------
        }
        txt_c2diff.setText(time_readingCP2);
        status_detailCP2 = s10_CP2;
        unit_detailCP2 = meterreadingCP2;
        etime_detailCP2 = time_readingCP2;
        // sPisPoweFail.setisPowerFailCP1("t");
        relativeLayout2nd.setVisibility(View.GONE);
        } catch (NumberFormatException ex) {

        }


        } else if (plugedinCountcp2 == 0) {
        tv_status2.setText(s11_CP2);
        isAvailableC2 = false;

        txt_c2round_off_mr.setText("₹ 00.00");
        rsCP2 = "₹ 00.00";
        txt_c2diff.setText("00:00:00");
        status_detailCP2 = s11_CP2;
        unit_detailCP2 = "00.00";
        etime_detailCP2 = "00:00:00";
        sPisPluggedin.setisPluggedinCP2("t");
        //-----------------meter reading
        sPispowerfailafter4mrCP2.setispowerfailafter4mrCP2("f");
        //-------------
        plugedinCountcp2++;

        //meter reading-------------------------
        sPmeterReadingCP2.setMeterReadingCP2(p2meter);
        //Time reading_________________
        StartTimeCP2 = SystemClock.uptimeMillis() + (-NewBeginMillsCP2);  //--> Start Time
        btn_p2onff.startAnimation(anim);

        } else {
        //time reading______
        MillisCP2 = (SystemClock.uptimeMillis() - StartTimeCP2);

        HoursCP2 = (int) (MillisCP2 / (1000 * 60 * 60));
        MinutesCP2 = (int) (MillisCP2 / (1000 * 60)) % 60;
        SecondsCP2 = (int) (MillisCP2 / 1000) % 60;
        String time_readingCP2 = "" + String.format("%02d", HoursCP2) + ":"
        + String.format("%02d", MinutesCP2) + ":"
        + String.format("%02d", SecondsCP2);


        //-----------meter Reading
        float prev_mrCP2 = Float.parseFloat(sPmeterReadingCP2.getMeterReadingCP2());
        float current_mrCP2 = 0.0f;
        try {


        Log.e("Metervaluesbefore", "prev_mrCP2" + prev_mrCP2 + "current_mrCP2 " + current_mrCP2);
        if (sPispowerfailafter4mrCP2.getispowerfailafter4mrCP2().equals("t")) {
        current_mrCP2 = (Float.parseFloat(sPmeterReadingCP2RO.getMeterReadingCP2RO()) * 4) + Float.parseFloat(p2meter);
        //current_mrCP1 = Float.parseFloat("4.00")+Float.parseFloat(p1meter);
        Log.e("current_mrCP2insideadd", "" + current_mrCP2 + " b ");
        }
        if (sPispowerfailafter4mrCP2.getispowerfailafter4mrCP2().equals("f")) {
        current_mrCP2 = Float.parseFloat(p2meter);
        int b = (int) current_mrCP2;
        b = b / 4;
        sPmeterReadingCP2RO.setMeterReadingCP2RO("" + b);
        Log.e("current_mrCP2inside", "" + current_mrCP2 + " b " + b);

        }
        Log.e("Metervaluesafter", "prev_mrCP2" + prev_mrCP2 + "current_mrCP2 " + current_mrCP2);

        float meterreadingdiff2 = current_mrCP2 - prev_mrCP2;
        float meterreadingdiffP2 = 0.0f;
        meterreadingdiffP2 = (current_mrCP2 - prev_mrCP2) * erate;

        Log.e("SAVEDMONEYFLAGINSIDE", sPispowerfailafter4mrCP2.getispowerfailafter4mrCP2());
        String meterreadingCP2 = String.format("%.2f", meterreadingdiff2);
        String meterreadingCP2P = String.format("%.2f", meterreadingdiffP2);

        //--------------------
        tv_status2.setText(s11_CP2);
        isAvailableC2 = false;

        if (meterreadingdiffP2 > 0 && meterreadingdiffP2 < 200) {
        txt_c2round_off_mr.setText("₹ " + meterreadingCP2P);
        rsCP2 = "₹ " + meterreadingCP2P;
        rsPOCP2 = "₹ " + meterreadingCP2P;
        //------------
        float mrv = meterreadingdiffP2;
        mrsPOCP2 = "" + mrv;
        //-----------
        }
        txt_c2diff.setText(time_readingCP2);
        status_detailCP2 = s11_CP2;
        unit_detailCP2 = meterreadingCP2;
        etime_detailCP2 = time_readingCP2;
        sPisPluggedin.setisPluggedinCP2("t");
        //---------------------
        sPispowerfailafter4mrCP2.setispowerfailafter4mrCP2("f");
        //-----------
        //sPisPoweFail.setisPowerFailCP1("t");
        // sPTimeReadingCP1.setTimeReadingCP1(MillisCP1);------------------TTTTTTTTTTTTTTTT
        //-------------------------------
        plugedinCountcp2++;
        } catch (NumberFormatException ex) {

        }
        }

        }

        }

//================================================================================================================================================================================================================
//=======================================================================================================================================================================================================================================================================================

        //-----------------------------------------------------------CP3 Status-----------------------------------------------------------------------------------------------------------------------------
        if (p3OnOff.equals("0") && p3plugin.equals("0") && p3fault.equals("99")) {

        if (sPisPluggedin.getisPluggedinCP3().equals("t")) {

        if (plugedoutCountcp3 >= 30) {
        sPisPluggedin.setisPluggedinCP3("f");

        } else {
        displayPlugoutCountC3 = 0;
        tv_status3.setText(s7_CP3);
        isAvailableC3 = true;

        txt_c3round_off_mr.setText(rsPOCP3);

        rsCP3 = "₹ 00.00";
        txt_c3diff.setText(etime_detailCP3);

        status_detailCP3 = s7_CP3;
        unit_detailCP3 = unit_detailCP3;
        etime_detailCP3 = etime_detailCP3;
        // sPispowerfailafter4mrCP1.setispowerfailafter4mrCP1("f");

        plugedinCountcp3 = 0;
        plugedoutCountcp3++;
        if (isResumedAftercp3) {
        sPisPoweFail.setisPowerFailCP3("f");
        sPispowerfailafter4mrCP3.setispowerfailafter4mrCP3("f");

        }
        }

        } else {
        countidleCP3++;
        if (isPleaseWaitC3) {
        tv_status3.setText("Please Wait...");
        status_detailCP3 = "Please Wait...";
        btn_p3onff.setVisibility(View.INVISIBLE);

        } else {
        tv_status3.setText(s8_CP3);
        status_detailCP3 = s8_CP3;

        }


        if (isStopC3) {
        //   countC1 = 0;
        isAvailableC3 = true;

        isStartC3 = true;
        isStopC3 = false;
        }

        txt_c3round_off_mr.setText("₹ 00.00");
        rsCP3 = "₹ 00.00";
        rsPOCP3 = "₹ 00.00";
        //------------
        float mrv = 0;
        mrsPOCP3 = "" + mrv;
        //-----------
        txt_c3diff.setText("00:00:00");
        unit_detailCP3 = "00.00";
        etime_detailCP3 = "00:00:00";
        plugedinCountcp3 = 0;
        btn_p3onff.clearAnimation();
        //overlay1
        if (overlayCountiii > 10) {
        isPleaseWaitC3 = false;
        relativeLayout3rd.setVisibility(View.VISIBLE);
        }
        overlayCountiii++;
        }

        }
        if (p3OnOff.equals("1") && p3plugin.equals("0") && p3fault.equals("99")) {

        countidleCP3 = 0;
        plugedinCountcp3 = 0;
        plugedoutCountcp3 = 0;
        tv_status3.setText(s1_CP3);
        isTappedC3 = false;
        isPleaseWaitC3 = false;

        if (isStartC3) {
        isStartC3 = false;
        countC3++;
        isStopC3 = true;
        isAvailableC3 = false;
        }
        txt_c3round_off_mr.setText("₹ 00.00");
        rsCP3 = "₹ 00.00";
        txt_c3diff.setText("00:00:00");
        status_detailCP3 = s1_CP3;
        unit_detailCP3 = "00.00";
        etime_detailCP3 = "00:00:00";
        isStillOnCP3 = false;
        relativeLayout3rd.setVisibility(View.GONE);
        btn_p3onff.setVisibility(View.VISIBLE);

        if (displayPlugoutCountC3 == 12) {
        if (sPisPoweFail.getisPowerFailCP3().equals("t") && sPisPluggedin.getisPluggedinCP3().equals("t")) {
        btn_p3onff.performClick();
        sPisPoweFail.setisPowerFailCP3("f");
        sPisPluggedin.setisPluggedinCP3("f");
        tv_status3.setText(s7_CP3);

        status_detailCP3 = s7_CP3;
        // sPispowerfailafter4mrCP1.setispowerfailafter4mrCP1("f");

        //-----------
        sPisPoweFail.setisPowerFailCP3("f");
        // sPTimeReadingCP1.setTimeReadingCP1(MillisCP1);
        }
        }
        displayPlugoutCountC3++;
        //------------------===============================================================
                                                       */
/* sPisPoweFail.setisPowerFailCP3("t");
                                                        sPTimeReadingCP3.setTimeReadingCP3(MillisCP3);*//*


        }
        if (p3OnOff.equals("1") && p3plugin.equals("1") && p3fault.equals("99")) {

        countidleCP3 = 0;
        if (sPisPoweFail.getisPowerFailCP3().equals("t")) {
        //---------Status:resuming after power fail_______________
        if (plugedinCountcp3 >= 10) {
        //time reading______
        MillisCP3 = (SystemClock.uptimeMillis() - StartTimeCP3);

        HoursCP3 = (int) (MillisCP3 / (1000 * 60 * 60));
        MinutesCP3 = (int) (MillisCP3 / (1000 * 60)) % 60;
        SecondsCP3 = (int) (MillisCP3 / 1000) % 60;
        String time_readingCP3 = "" + String.format("%02d", HoursCP3) + ":"
        + String.format("%02d", MinutesCP3) + ":"
        + String.format("%02d", SecondsCP3);
        //-------------------------meter reading
        float prev_mrCP3 = Float.parseFloat(sPmeterReadingCP3.getMeterReadingCP3());
        float current_mrCP3 = 0.0f;
        Log.e("Metervaluesbefore", "prev_mrCP3" + prev_mrCP3 + "current_mrCP3 " + current_mrCP3);
        try {


        if (sPispowerfailafter4mrCP3.getispowerfailafter4mrCP3().equals("t")) {
        current_mrCP3 = (Float.parseFloat(sPmeterReadingCP3RO.getMeterReadingCP3RO()) * 4) + Float.parseFloat(p3meter);
        //current_mrCP1 = Float.parseFloat("4.00")+Float.parseFloat(p1meter);
        Log.e("current_mrCP3insideadd", "" + current_mrCP3 + " b ");
        }
        if (sPispowerfailafter4mrCP3.getispowerfailafter4mrCP3().equals("f")) {
        current_mrCP3 = Float.parseFloat(p3meter);
        int b = (int) current_mrCP3;
        b = b / 4;
        sPmeterReadingCP3RO.setMeterReadingCP3RO("" + b);
        Log.e("current_mrCP3inside", "" + current_mrCP3 + " b " + b);

        }
        Log.e("Metervaluesafter", "prev_mrCP3" + prev_mrCP3 + "current_mrCP3 " + current_mrCP3);

        float meterreadingdiff3 = current_mrCP3 - prev_mrCP3;
        float meterreadingdiffP3 = 0.0f;
        meterreadingdiffP3 = (current_mrCP3 - prev_mrCP3) * erate;

        Log.e("SAVEDMONEYINSIDE", "" + meterreadingdiffP3);
        Log.e("SAVEDMONEYFLAGINSIDE", sPispowerfailafter4mrCP3.getispowerfailafter4mrCP3());
        String meterreadingCP3 = String.format("%.2f", meterreadingdiff3);
        String meterreadingCP3P = String.format("%.2f", meterreadingdiffP3);

        //------------------
        tv_status3.setText(s10_CP3);
        // isSavedTimeC3 = false;
        if (meterreadingdiffP3 > 0 && meterreadingdiffP3 < 200) {
        txt_c3round_off_mr.setText("₹ " + meterreadingCP3P);
        rsCP3 = "₹ " + meterreadingCP3P;
        rsPOCP3 = "₹ " + meterreadingCP3P;
        //------------
        float mrv = meterreadingdiffP3;
        mrsPOCP3 = "" + mrv;
        //-----------
        }
        txt_c3diff.setText(time_readingCP3);
        status_detailCP3 = s10_CP3;
        unit_detailCP3 = meterreadingCP3;
        etime_detailCP3 = time_readingCP3;
        sPisPoweFail.setisPowerFailCP3("f");
        relativeLayout3rd.setVisibility(View.GONE);
        } catch (NumberFormatException ex) {

        }

        } else if (plugedinCountcp3 == 0) {
        //Time reading_________________

        StartTimeCP3 = SystemClock.uptimeMillis() + (-sPTimeReadingCP3.getTimeReadingCP3());
        //-------------------------meter reading
        float prev_mrCP3 = Float.parseFloat(sPmeterReadingCP3.getMeterReadingCP3());
        float current_mrCP3 = 0.0f;
        try {


        Log.e("Metervaluesbefore", "prev_mrCP3" + prev_mrCP3 + "current_mrCP3 " + current_mrCP3);
        if (sPispowerfailafter4mrCP3.getispowerfailafter4mrCP3().equals("t")) {
        current_mrCP3 = (Float.parseFloat(sPmeterReadingCP3RO.getMeterReadingCP3RO()) * 4) + Float.parseFloat(p3meter);
        //current_mrCP1 = Float.parseFloat("4.00")+Float.parseFloat(p1meter);
        Log.e("current_mrCP3insideadd", "" + current_mrCP3 + " b ");
        }
        if (sPispowerfailafter4mrCP3.getispowerfailafter4mrCP3().equals("f")) {
        current_mrCP3 = Float.parseFloat(p3meter);
        int b = (int) current_mrCP3;
        b = b / 4;
        sPmeterReadingCP3RO.setMeterReadingCP3RO("" + b);
        Log.e("current_mrCP3inside", "" + current_mrCP3 + " b " + b);

        }
        Log.e("Metervaluesafter", "prev_mrCP2" + prev_mrCP3 + "current_mrCP2 " + current_mrCP3);

        float meterreadingdiff3 = current_mrCP3 - prev_mrCP3;
        float meterreadingdiffP3 = 0.0f;
        meterreadingdiffP3 = (current_mrCP3 - prev_mrCP3) * erate;

        Log.e("SAVEDMONEYINSIDE", "" + meterreadingdiffP3);
        Log.e("SAVEDMONEYFLAGINSIDE", sPispowerfailafter4mrCP3.getispowerfailafter4mrCP3());
        String meterreadingCP3 = String.format("%.2f", meterreadingdiff3);
        String meterreadingCP3P = String.format("%.2f", meterreadingdiffP3);
        //--------------------
        tv_status3.setText(s9_CP3);
        if (meterreadingdiffP3 > 0 && meterreadingdiffP3 < 200) {
        txt_c3round_off_mr.setText("₹ " + meterreadingCP3P);
        rsCP3 = "₹ " + meterreadingCP3P;
        rsPOCP3 = "₹ " + meterreadingCP3P;
        //------------
        float mrv = meterreadingdiffP3;
        mrsPOCP3 = "" + mrv;
        //-----------
        }
        txt_c3diff.setText("00:00:00");
        status_detailCP3 = s9_CP3;
        unit_detailCP3 = meterreadingCP3;
        etime_detailCP3 = "00:00:00";
        plugedinCountcp3++;
        sPisPluggedin.setisPluggedinCP3("t");
        isResumedAftercp3 = true;
        btn_p3onff.startAnimation(anim);
        relativeLayout3rd.setVisibility(View.GONE);
        isAvailableC3 = false;

        } catch (NumberFormatException ex) {

        }


        } else {
        //time reading______
        MillisCP3 = (SystemClock.uptimeMillis() - StartTimeCP3);

        HoursCP3 = (int) (MillisCP3 / (1000 * 60 * 60));
        MinutesCP3 = (int) (MillisCP3 / (1000 * 60)) % 60;
        SecondsCP3 = (int) (MillisCP3 / 1000) % 60;
        String time_readingCP3 = "" + String.format("%02d", HoursCP3) + ":"
        + String.format("%02d", MinutesCP3) + ":"
        + String.format("%02d", SecondsCP3);

        //-------------------------meter reading
        float prev_mrCP3 = Float.parseFloat(sPmeterReadingCP3.getMeterReadingCP3());
        float current_mrCP3 = 0.0f;
        try {
        Log.e("Metervaluesbefore", "prev_mrCP3" + prev_mrCP3 + "current_mrCP3 " + current_mrCP3);
        if (sPispowerfailafter4mrCP3.getispowerfailafter4mrCP3().equals("t")) {
        current_mrCP3 = (Float.parseFloat(sPmeterReadingCP3RO.getMeterReadingCP3RO()) * 4) + Float.parseFloat(p3meter);
        //current_mrCP1 = Float.parseFloat("4.00")+Float.parseFloat(p1meter);
        Log.e("current_mrCP3insideadd", "" + current_mrCP3 + " b ");
        }
        if (sPispowerfailafter4mrCP3.getispowerfailafter4mrCP3().equals("f")) {
        current_mrCP3 = Float.parseFloat(p3meter);
        int b = (int) current_mrCP3;
        b = b / 4;
        sPmeterReadingCP3RO.setMeterReadingCP3RO("" + b);
        Log.e("current_mrCP3inside", "" + current_mrCP3 + " b " + b);

        }
        Log.e("Metervaluesafter", "prev_mrCP3" + prev_mrCP3 + "current_mrCP3 " + current_mrCP3);

        float meterreadingdiff3 = current_mrCP3 - prev_mrCP3;
        float meterreadingdiffP3 = 0.0f;
        meterreadingdiffP3 = (current_mrCP3 - prev_mrCP3) * erate;

        Log.e("SAVEDMONEYINSIDE", "" + meterreadingdiffP3);
        Log.e("SAVEDMONEYFLAGINSIDE", sPispowerfailafter4mrCP3.getispowerfailafter4mrCP3());
        String meterreadingCP3 = String.format("%.2f", meterreadingdiff3);
        String meterreadingCP3P = String.format("%.2f", meterreadingdiffP3);
        //--------------------

        tv_status3.setText(s9_CP3);
        isAvailableC3 = false;

        if (meterreadingdiffP3 > 0 && meterreadingdiffP3 < 200) {
        txt_c3round_off_mr.setText("₹ " + meterreadingCP3P);
        rsCP3 = "₹ " + meterreadingCP3P;
        rsPOCP3 = "₹ " + meterreadingCP3P;
        //------------
        float mrv = meterreadingdiffP3;
        mrsPOCP3 = "" + mrv;
        //-----------
        }
        txt_c3diff.setText(time_readingCP3);
        status_detailCP3 = s9_CP3;
        unit_detailCP3 = meterreadingCP3;
        etime_detailCP3 = time_readingCP3;
        plugedinCountcp3++;
        sPisPluggedin.setisPluggedinCP3("t");
        isResumedAftercp3 = true;
        relativeLayout3rd.setVisibility(View.GONE);
        } catch (NumberFormatException ex) {

        }

        }


        } else {
        //--------------- Status:vehicle pugged in_________
        if (plugedinCountcp3 >= 10) {
        //time reading______
        MillisCP3 = (SystemClock.uptimeMillis() - StartTimeCP3);

        HoursCP3 = (int) (MillisCP3 / (1000 * 60 * 60));
        MinutesCP3 = (int) (MillisCP3 / (1000 * 60)) % 60;
        SecondsCP3 = (int) (MillisCP3 / 1000) % 60;
        String time_readingCP3 = "" + String.format("%02d", HoursCP3) + ":"
        + String.format("%02d", MinutesCP3) + ":"
        + String.format("%02d", SecondsCP3);

        //-------------------------meter reading
        float prev_mrCP3 = Float.parseFloat(sPmeterReadingCP3.getMeterReadingCP3());
        float current_mrCP3 = 0.0f;
        Log.e("Metervaluesbefore", "prev_mrCP2" + prev_mrCP3 + "current_mrCP3 " + current_mrCP3);
        try {


        if (sPispowerfailafter4mrCP3.getispowerfailafter4mrCP3().equals("t")) {
        current_mrCP3 = (Float.parseFloat(sPmeterReadingCP3RO.getMeterReadingCP3RO()) * 4) + Float.parseFloat(p3meter);
        //current_mrCP1 = Float.parseFloat("4.00")+Float.parseFloat(p1meter);
        Log.e("current_mrCP2insideadd", "" + current_mrCP3 + " b ");
        }
        if (sPispowerfailafter4mrCP3.getispowerfailafter4mrCP3().equals("f")) {
        current_mrCP3 = Float.parseFloat(p3meter);
        int b = (int) current_mrCP3;
        b = b / 4;
        sPmeterReadingCP3RO.setMeterReadingCP3RO("" + b);
        Log.e("current_mrCP3inside", "" + current_mrCP3 + " b " + b);

        }

        Log.e("Metervaluesafter", "prev_mrCP3" + prev_mrCP3 + "current_mrCP3 " + current_mrCP3);

        float meterreadingdiff3 = current_mrCP3 - prev_mrCP3;
        float meterreadingdiffP3 = 0.0f;
        meterreadingdiffP3 = (current_mrCP3 - prev_mrCP3) * erate;

        Log.e("SAVEDMONEYINSIDE", "" + meterreadingdiffP3);
        Log.e("SAVEDMONEYFLAGINSIDE", sPispowerfailafter4mrCP3.getispowerfailafter4mrCP3());
        String meterreadingCP3 = String.format("%.2f", meterreadingdiff3);
        String meterreadingCP3P = String.format("%.2f", meterreadingdiffP3);

        tv_status3.setText(s10_CP3);
        //isSavedTimeC3 = false;
        if (meterreadingdiffP3 > 0 && meterreadingdiffP3 < 200) {
        txt_c3round_off_mr.setText("₹ " + meterreadingCP3P);
        rsCP3 = "₹ " + meterreadingCP3P;
        rsPOCP3 = "₹ " + meterreadingCP3P;
        //------------
        float mrv = meterreadingdiffP3;
        mrsPOCP3 = "" + mrv;
        //-----------
        }
        txt_c3diff.setText(time_readingCP3);
        status_detailCP3 = s10_CP3;
        unit_detailCP3 = meterreadingCP3;
        etime_detailCP3 = time_readingCP3;
        // sPisPoweFail.setisPowerFailCP1("t");
        relativeLayout3rd.setVisibility(View.GONE);
        } catch (NumberFormatException ex) {

        }


        } else if (plugedinCountcp3 == 0) {
        tv_status3.setText(s11_CP3);
        isAvailableC3 = false;

        txt_c3round_off_mr.setText("₹ 00.00");
        rsCP3 = "₹ 00.00";
        txt_c3diff.setText("00:00:00");
        status_detailCP3 = s11_CP3;
        unit_detailCP3 = "00.00";
        etime_detailCP3 = "00:00:00";
        sPisPluggedin.setisPluggedinCP3("t");
        //-----------------meter reading
        sPispowerfailafter4mrCP3.setispowerfailafter4mrCP3("f");
        //-------------
        plugedinCountcp3++;

        //meter reading-------------------------
        sPmeterReadingCP3.setMeterReadingCP3(p3meter);
        //Time reading_________________
        StartTimeCP3 = SystemClock.uptimeMillis() + (-NewBeginMillsCP3);  //--> Start Time
        btn_p3onff.startAnimation(anim);

        } else {
        //time reading______
        MillisCP3 = (SystemClock.uptimeMillis() - StartTimeCP3);

        HoursCP3 = (int) (MillisCP3 / (1000 * 60 * 60));
        MinutesCP3 = (int) (MillisCP3 / (1000 * 60)) % 60;
        SecondsCP3 = (int) (MillisCP3 / 1000) % 60;
        String time_readingCP3 = "" + String.format("%02d", HoursCP3) + ":"
        + String.format("%02d", MinutesCP3) + ":"
        + String.format("%02d", SecondsCP3);


        //-----------meter Reading
        float prev_mrCP3 = Float.parseFloat(sPmeterReadingCP3.getMeterReadingCP3());
        float current_mrCP3 = 0.0f;
        try {


        Log.e("Metervaluesbefore", "prev_mrCP3" + prev_mrCP3 + "current_mrCP3 " + current_mrCP3);
        if (sPispowerfailafter4mrCP3.getispowerfailafter4mrCP3().equals("t")) {
        current_mrCP3 = (Float.parseFloat(sPmeterReadingCP3RO.getMeterReadingCP3RO()) * 4) + Float.parseFloat(p3meter);
        //current_mrCP1 = Float.parseFloat("4.00")+Float.parseFloat(p1meter);
        Log.e("current_mrCP3insideadd", "" + current_mrCP3 + " b ");
        }
        if (sPispowerfailafter4mrCP3.getispowerfailafter4mrCP3().equals("f")) {
        current_mrCP3 = Float.parseFloat(p3meter);
        int b = (int) current_mrCP3;
        b = b / 4;
        sPmeterReadingCP3RO.setMeterReadingCP3RO("" + b);
        Log.e("current_mrCP3inside", "" + current_mrCP3 + " b " + b);

        }
        Log.e("Metervaluesafter", "prev_mrCP3" + prev_mrCP3 + "current_mrCP3 " + current_mrCP3);

        float meterreadingdiff3 = current_mrCP3 - prev_mrCP3;
        float meterreadingdiffP3 = 0.0f;
        meterreadingdiffP3 = (current_mrCP3 - prev_mrCP3) * erate;

        Log.e("SAVEDMONEYFLAGINSIDE", sPispowerfailafter4mrCP3.getispowerfailafter4mrCP3());
        String meterreadingCP3 = String.format("%.2f", meterreadingdiff3);
        String meterreadingCP3P = String.format("%.2f", meterreadingdiffP3);

        //--------------------
        tv_status3.setText(s11_CP3);
        isAvailableC3 = false;

        if (meterreadingdiffP3 > 0 && meterreadingdiffP3 < 200) {
        txt_c3round_off_mr.setText("₹ " + meterreadingCP3P);
        rsCP3 = "₹ " + meterreadingCP3P;
        rsPOCP3 = "₹ " + meterreadingCP3P;
        //------------
        float mrv = meterreadingdiffP3;
        mrsPOCP3 = "" + mrv;
        //-----------
        }
        txt_c3diff.setText(time_readingCP3);
        status_detailCP3 = s11_CP3;
        unit_detailCP3 = meterreadingCP3;
        etime_detailCP3 = time_readingCP3;
        sPisPluggedin.setisPluggedinCP3("t");
        //--------------------
        sPispowerfailafter4mrCP3.setispowerfailafter4mrCP3("f");
        //-----------
        //sPisPoweFail.setisPowerFailCP1("t");
        // sPTimeReadingCP1.setTimeReadingCP1(MillisCP1);------------------TTTTTTTTTTTTTTTT
        //-------------------------------
        plugedinCountcp3++;
        } catch (NumberFormatException ex) {

        }
        }

        }

        }

//================================================================================================================================================================================================================

        if (detail_flag.equals("d_CP1")) {
        display_details(rsCP1, s15_CP1 + " #01", voltage_detailCP1, current_detailCP1, power_detailCP1, unit_detailCP1, etime_detailCP1, status_detailCP1, s14_CP1, s17_CP1, " ₹ " + erate_s + " " + s16_CP1, sPlanguageCP1.getlanguageCP1());

        } else if (detail_flag.equals("d_CP2")) {
        display_details(rsCP2, s15_CP2 + " #02", voltage_detailCP2, current_detailCP2, power_detailCP2, unit_detailCP2, etime_detailCP2, status_detailCP2, s14_CP2, s17_CP2, " ₹ " + erate_s + " " + s16_CP2, sPlanguageCP2.getlanguageCP2());

        } else if (detail_flag.equals("d_CP3")) {
        display_details(rsCP3, s15_CP3 + " #03", voltage_detailCP3, current_detailCP3, power_detailCP3, unit_detailCP3, etime_detailCP3, status_detailCP3, s14_CP3, s17_CP3, " ₹ " + erate_s + " " + s16_CP3, sPlanguageCP3.getlanguageCP3());

        }

        Log.e("CHECKII", "CHECKING");

        Log.e("COMMING STRING", recDataString + "\n\np1OnOff :" + p1OnOff + "\tp1plugin :" + p1plugin + "\tp1fault :" + p1fault + "\tp1voltage :" + p1voltage + "\tp1current :" + p1current + "\tp1meter :" + p1meter +
        "\n\np2OnOff : " + p2OnOff + "\tp2plugin : " + p2plugin + "\tp2fault : " + p2fault + "\tp2voltage : " + p2voltage + "\tp2current : " + p2current + "\tp2meter : " + p2meter +
        "\n\np3OnOff : " + p3OnOff + "\tp3plugin : " + p3plugin + "\tp3fault : " + p3fault + "\tp3voltage: " + p3voltage + "\tp3current : " + p3current + "\tp3meter : " + p3meter);

        }
        }

        } catch (StringIndexOutOfBoundsException siobe) {
        //System.out.println("invalid input");
        }

        }
        }

        recDataString.delete(0, recDataString.length());                    //clear all string data

        }
                                */
/*else{
                                    recDataString.delete(0, recDataString.length());
                                }*//*

        //  }======================================================================================
                       */
/* else{
                            recDataString.delete(0, recDataString.length());                    //clear all string data

                        }*//*

        } else {
        tv_status1.setText(s6_CP1);
        String s = globalP1meter;
        float v = Float.parseFloat(s);
        int b = (int) v;
        if (b >= 4) {
        sPispowerfailafter4mrCP1.setispowerfailafter4mrCP1("t");
        //sPsavemoneyafterpfCP1.setMeterReadingCP1RO(mrsPOCP1);
        sPsavemoneyafterpfCP1.setMeterReadingCP1RO("4.00");

        }
        tv_status2.setText(s6_CP2);

        String s2 = globalP2meter;
        float v2 = Float.parseFloat(s2);
        int b2 = (int) v2;
        if (b2 >= 4) {
        sPispowerfailafter4mrCP2.setispowerfailafter4mrCP2("t");
        }
        tv_status3.setText(s6_CP3);

        String s3 = globalP3meter;
        float v3 = Float.parseFloat(s3);
        int b3 = (int) v3;
        if (b3 >= 4) {
        sPispowerfailafter4mrCP3.setispowerfailafter4mrCP3("t");
        }
        toggleBtn.setCardBackgroundColor(Color.parseColor(red));
        toggleBtn.startAnimation(anim);
        btn_p2onff.setCardBackgroundColor(Color.parseColor(red));
        btn_p2onff.startAnimation(anim);
        btn_p3onff.setCardBackgroundColor(Color.parseColor(red));
        btn_p3onff.startAnimation(anim);
        if (sPisPluggedin.getisPluggedinCP1().equals("t")) {
        sPisPoweFail.setisPowerFailCP1("t");
        sPTimeReadingCP1.setTimeReadingCP1(MillisCP1);
        //  isSavedTimeC1 = true;


        }
        if (sPisPluggedin.getisPluggedinCP2().equals("t")) {
        sPisPoweFail.setisPowerFailCP2("t");
        sPTimeReadingCP2.setTimeReadingCP2(MillisCP2);
        // isSavedTimeC2 = true;

        }
        if (sPisPluggedin.getisPluggedinCP3().equals("t")) {
        sPisPoweFail.setisPowerFailCP3("t");
        sPTimeReadingCP3.setTimeReadingCP3(MillisCP3);
        // isSavedTimeC3 = true;

        }
        }
        }*/
