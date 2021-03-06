package ir.tiroon.android.messenger.client.view;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.tiroon.android.messenger.client.service.SimpleWebSocketEchoService;
import ir.tiroon.android.messenger.client.R;
import ir.tiroon.android.messenger.client.modelview.MainActivityUserViewModel;
import ir.tiroon.android.messenger.client.service.RabbitMQService;

public class MainFragment extends Fragment {

    private MainActivityUserViewModel mMainActivityViewModel;

    @BindView(R.id.serverIdEditText)
    EditText serverIdEditText;

    @BindView(R.id.usernameEditText)
    EditText usernameEditText;

    @BindView(R.id.passwordEditText)
    EditText passwordEditText;

    @BindView(R.id.amqpConnectButton)
    Button amqpConnectButton;

    @BindView(R.id.simpleWebSocketConnectButton)
    Button simpleWebSocketConnectButton;

    SimpleWebSocketEchoService simpleWebSocketEchoService;
    RabbitMQService rabbitMQService;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMainActivityViewModel = ViewModelProviders.of(this).get(MainActivityUserViewModel.class);
        mMainActivityViewModel.populateWithData();
        mMainActivityViewModel.updateEchoMessage("rabbit mq amqp messages coming");
        amqpConnectButton.setOnClickListener(v -> {
            String userName = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String ip = serverIdEditText.getText().toString();
            String queueName = "bmd579@gmail.com";
            rabbitMQService = new RabbitMQService(ip, userName, password, queueName, amqpConnectButton::setText);
            amqpConnectButton.setClickable(false);
        });

        /////////////////////
        simpleWebSocketConnectButton.setOnClickListener(v -> {
            simpleWebSocketEchoService = new SimpleWebSocketEchoService("wss://echo.websocket.org", mMainActivityViewModel::updateEchoMessage);
            new AsyncTask<Void, Void, Void>() {
                int i = 1;
                @Override
                protected Void doInBackground(Void... voids) {
                    while (i<10000){
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        i = i * 10;
                        simpleWebSocketEchoService.sendText("please echo number "+i);
                    }
                    return null;
                }
            }.execute();
            simpleWebSocketConnectButton.setClickable(false);
        });

        mMainActivityViewModel.getEchoMessage().observe(this, simpleWebSocketConnectButton::setText);
    }

}
