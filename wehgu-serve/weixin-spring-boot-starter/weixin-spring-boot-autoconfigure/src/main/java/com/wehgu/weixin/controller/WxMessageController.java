package com.wehgu.weixin.controller;

import com.jfinal.aop.Before;
import com.jfinal.weixin.iot.msg.InEquDataMsg;
import com.jfinal.weixin.iot.msg.InEqubindEvent;
import com.jfinal.weixin.sdk.jfinal.MsgControllerAdapter;
import com.jfinal.weixin.sdk.jfinal.MsgInterceptor;
import com.jfinal.weixin.sdk.msg.in.*;
import com.jfinal.weixin.sdk.msg.in.card.*;
import com.jfinal.weixin.sdk.msg.in.event.*;
import com.jfinal.weixin.sdk.msg.in.speech_recognition.InSpeechRecognitionResults;

public class WxMessageController extends MsgControllerAdapter {

    @Before(MsgInterceptor.class)
    @Override
    public void index() {
        super.index();
        renderNull();
    }

    @Override
    protected void processInFollowEvent(InFollowEvent inFollowEvent) {

    }

    @Override
    protected void processInTextMsg(InTextMsg inTextMsg) {

    }

    @Override
    protected void processInMenuEvent(InMenuEvent inMenuEvent) {

    }

    @Override
    protected void processInCardPassCheckEvent(InCardPassCheckEvent msg) {
        super.processInCardPassCheckEvent(msg);
    }

    @Override
    protected void processInCardSkuRemindEvent(InCardSkuRemindEvent msg) {
        super.processInCardSkuRemindEvent(msg);
    }

    @Override
    protected void processInEqubindEvent(InEqubindEvent msg) {
        super.processInEqubindEvent(msg);
    }

    @Override
    protected void processInCardPayOrderEvent(InCardPayOrderEvent msg) {
        super.processInCardPayOrderEvent(msg);
    }

    @Override
    protected void processInCustomEvent(InCustomEvent inCustomEvent) {
        super.processInCustomEvent(inCustomEvent);
    }

    @Override
    protected void processInEquDataMsg(InEquDataMsg msg) {
        super.processInEquDataMsg(msg);
    }

    @Override
    protected void processInImageMsg(InImageMsg inImageMsg) {
        super.processInImageMsg(inImageMsg);
    }

    @Override
    protected void processInLinkMsg(InLinkMsg inLinkMsg) {
        super.processInLinkMsg(inLinkMsg);
    }

    @Override
    protected void processInLocationEvent(InLocationEvent inLocationEvent) {
        super.processInLocationEvent(inLocationEvent);
    }

    @Override
    protected void processInLocationMsg(InLocationMsg inLocationMsg) {
        super.processInLocationMsg(inLocationMsg);
    }

    @Override
    protected void processInMassEvent(InMassEvent inMassEvent) {
        super.processInMassEvent(inMassEvent);
    }

    @Override
    protected void processInMerChantOrderEvent(InMerChantOrderEvent inMerChantOrderEvent) {
        super.processInMerChantOrderEvent(inMerChantOrderEvent);
    }

    @Override
    protected void processInPoiCheckNotifyEvent(InPoiCheckNotifyEvent inPoiCheckNotifyEvent) {
        super.processInPoiCheckNotifyEvent(inPoiCheckNotifyEvent);
    }

    @Override
    protected void processInQrCodeEvent(InQrCodeEvent inQrCodeEvent) {
        super.processInQrCodeEvent(inQrCodeEvent);
    }

    @Override
    protected void processInShakearoundUserShakeEvent(InShakearoundUserShakeEvent inShakearoundUserShakeEvent) {
        super.processInShakearoundUserShakeEvent(inShakearoundUserShakeEvent);
    }

    @Override
    protected void processInShortVideoMsg(InShortVideoMsg inShortVideoMsg) {
        super.processInShortVideoMsg(inShortVideoMsg);
    }

    @Override
    protected void processInSpeechRecognitionResults(InSpeechRecognitionResults inSpeechRecognitionResults) {
        super.processInSpeechRecognitionResults(inSpeechRecognitionResults);
    }

    @Override
    protected void processInTemplateMsgEvent(InTemplateMsgEvent inTemplateMsgEvent) {
        super.processInTemplateMsgEvent(inTemplateMsgEvent);
    }

    @Override
    protected void processInUpdateMemberCardEvent(InUpdateMemberCardEvent msg) {
        super.processInUpdateMemberCardEvent(msg);
    }

    @Override
    protected void processInUserCardEvent(InUserCardEvent inUserCardEvent) {
        super.processInUserCardEvent(inUserCardEvent);
    }

    @Override
    protected void processInUserConsumeCardEvent(InUserConsumeCardEvent msg) {
        super.processInUserConsumeCardEvent(msg);
    }

    @Override
    protected void processInUserGetCardEvent(InUserGetCardEvent msg) {
        super.processInUserGetCardEvent(msg);
    }

    @Override
    protected void processInUserGiftingCardEvent(InUserGiftingCardEvent msg) {
        super.processInUserGiftingCardEvent(msg);
    }

    @Override
    protected void processInUserPayFromCardEvent(InUserPayFromCardEvent msg) {
        super.processInUserPayFromCardEvent(msg);
    }

    @Override
    protected void processInVerifyFailEvent(InVerifyFailEvent inVerifyFailEvent) {
        super.processInVerifyFailEvent(inVerifyFailEvent);
    }

    @Override
    protected void processInVerifySuccessEvent(InVerifySuccessEvent inVerifySuccessEvent) {
        super.processInVerifySuccessEvent(inVerifySuccessEvent);
    }

    @Override
    protected void processInVideoMsg(InVideoMsg inVideoMsg) {
        super.processInVideoMsg(inVideoMsg);
    }

    @Override
    protected void processInVoiceMsg(InVoiceMsg inVoiceMsg) {
        super.processInVoiceMsg(inVoiceMsg);
    }

    @Override
    protected void processInWifiEvent(InWifiEvent inWifiEvent) {
        super.processInWifiEvent(inWifiEvent);
    }

    @Override
    protected void processIsNotDefinedEvent(InNotDefinedEvent inNotDefinedEvent) {
        super.processIsNotDefinedEvent(inNotDefinedEvent);
    }

    @Override
    protected void processIsNotDefinedMsg(InNotDefinedMsg inNotDefinedMsg) {
        super.processIsNotDefinedMsg(inNotDefinedMsg);
    }
}