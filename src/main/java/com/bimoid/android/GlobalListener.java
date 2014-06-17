package com.bimoid.android;

import android.view.View;
import android.widget.ListView;

import com.obimp.OBIMP;
import com.obimp.cl.ContactListItem;
import com.obimp.listener.ConnectionListener;
import com.obimp.listener.ContactListListener;
import com.obimp.listener.MessageListener;
import com.obimp.listener.MetaInfoListener;
import com.obimp.listener.UserAvatarListener;
import com.obimp.listener.UserStatusListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by alex_xpert on 22.05.2014.
 */
public class GlobalListener implements ConnectionListener, ContactListListener, MessageListener, MetaInfoListener, UserStatusListener,
        UserAvatarListener {
    private View view;
    private ListView contact_list;
    private List<CLItem> wrapperList;
    private CLItem contact;
    private CLAdapter clAdapter;

    public GlobalListener(/*View view*/) {
        //this.view = view;
        //initData();
    }

    void initData() {
        contact_list = (ListView) view.findViewById(R.id.contact_list);
        clAdapter = new CLAdapter(view.getContext());
        wrapperList = new ArrayList<CLItem>();
        clAdapter.setData(wrapperList);
        contact_list.setAdapter(clAdapter);
    }

    public void setView(View vw) {
        this.view = vw;
        initData();
    }

    @Override
    public void onLoginSuccess() {

    }

    @Override
    public void onLoginFailed(String reason) {

    }

    @Override
    public void onLogout(String reason) {

    }

    @Override
    public void onAuthRequest(String userid, String reason) {

    }

    @Override
    public void onAuthReply(String userid, boolean reply) {

    }

    @Override
    public void onAuthRevoke(String userid, String reason) {

    }

    @Override
    public void onLoadContactList(ContactListItem[] cl) {

    }

    @Override
    public void onIncomingMessage(String id, String text) {

    }

    @Override
    public void onUserInfo(String result) {
        contact.setName(result.split("nick name: ")[1].split("\n")[0]);
        //OBIMP.reqAvatar(((MainActivity) activity).connection, contact.getAvatar());
        wrapperList.add(contact);
        clAdapter.setData(wrapperList);
        ((MainActivity) view.getContext()).handler.post( new Runnable() {
            @Override
            public void run() {
                contact_list.setAdapter(clAdapter);
            }
        } );
        contact = null;
    }

    @Override
    public void onSearch(String result) {

    }

    @Override
    public void onUserOnline(HashMap user) {
        contact = new CLItem();
        contact.setStatus(String.valueOf(user.get("Status")));
        //contact.setAvatar((byte[]) user.get("AvaHash"));
        OBIMP.get_cli_info(((MainActivity) view.getContext()).connection, String.valueOf(user.get("User")));
    }

    @Override
    public void onUserOffline(String id) {

    }

    @Override
    public void onAvatarReply(byte[] avatar) {
        //contact.setAvatar(avatar);
    }
}
