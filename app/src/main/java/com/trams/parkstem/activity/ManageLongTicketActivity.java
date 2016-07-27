package com.trams.parkstem.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.LinearLayout;

import com.trams.parkstem.R;
import com.trams.parkstem.base_activity.BaseBackSearchActivity;
import com.trams.parkstem.server.ServerClient;
import com.trams.parkstem.view.TicketView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Noverish on 2016-07-08.
 */
public class ManageLongTicketActivity extends BaseBackSearchActivity {
    private SwipeRefreshLayout swipeLayout;
    private ArrayList<TicketView> ticketViews = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_long_ticket);
        setSearchEnable(true);
        setToolbarTitle("정기권 관리");

        reloadServerData();

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.activity_manage_long_ticket_refresh_layout);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(true);
                reloadServerData();
                swipeLayout.setRefreshing(false);
            }
        });
    }

    private void reloadServerData() {
        try {
            ServerClient.LongTicketLists list = ServerClient.getInstance().listOfLongTicket(Calendar.getInstance());

            for (ServerClient.Ticket ticket : list.data) {
                TicketView ticketView = new TicketView(this, ticket, "상세정보", true, false, true);
                ticketViews.add(ticketView);
            }
        } catch (ServerClient.ServerErrorException ex) {
            Log.e("error!", ex.msg);
        }

        showData();
    }

    private void showData() {
        showSearchResult("");
    }

    private void showSearchResult(String result) {
        LinearLayout content = (LinearLayout) findViewById(R.id.activity_manage_long_ticket_layout);
        content.removeAllViews();

        for(TicketView ticketView : ticketViews)
            if(ticketView.getTicketName().contains(result))
                content.addView(ticketView);
    }

    @Override
    public boolean onClose() {
        showData();
        return super.onClose();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return super.onQueryTextSubmit(query);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        showSearchResult(newText);
        return super.onQueryTextChange(newText);
    }
}
