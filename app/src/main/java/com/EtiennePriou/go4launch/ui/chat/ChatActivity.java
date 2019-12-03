package com.EtiennePriou.go4launch.ui.chat;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.EtiennePriou.go4launch.R;
import com.EtiennePriou.go4launch.base.BaseActivity;
import com.EtiennePriou.go4launch.di.DI;
import com.EtiennePriou.go4launch.di.ViewModelFactory;
import com.EtiennePriou.go4launch.models.Message;
import com.EtiennePriou.go4launch.models.Workmate;
import com.EtiennePriou.go4launch.services.firebase.helpers.MessageHelper;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


public class ChatActivity extends BaseActivity implements MyChatRecyclerViewAdapter.Listener {

    private ChatViewModel mChatViewModel;

    private MyChatRecyclerViewAdapter mRecyclerViewAdapter;
    private String currentChatToken;
    private String receiver;

    private Workmate currentUser;

    private RecyclerView mRecyclerView;
    private EditText txtToSend;
    private TextView emptyChat;
    private TextView tvTitleChat;
    private Button btnSend;

    @Override
    protected int getLayoutContentViewID() {
        return R.layout.activity_chat;
    }

    @Override
    protected void setupUi() {
        mRecyclerView = findViewById(R.id.chat_recycler_view);
        txtToSend = findViewById(R.id.chat_message_edit_text);
        emptyChat = findViewById(R.id.tvEmptyChat);
        btnSend = findViewById(R.id.chat_send_button);
        tvTitleChat = findViewById(R.id.tvTitleChat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void withOnCreate() {
        setUpViewModel();
        currentUser = mFireBaseApi.getActualUser();
        currentChatToken = getIntent().getStringExtra(getResources().getString(R.string.TOKEN));
        receiver = getIntent().getStringExtra(getResources().getString(R.string.RECEIVER));
        String tmp = getResources().getString(R.string.your_chatting_with) + receiver;
        tvTitleChat.setText(tmp);
        configureRecyclerView();
        setUpSendBtn();
    }

    private void setUpViewModel() {
        ViewModelFactory viewModelFactory = DI.provideViewModelFactory();
        mChatViewModel = ViewModelProviders.of(this, viewModelFactory).get(ChatViewModel.class);
    }

    private void setUpSendBtn(){
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(txtToSend.getText()) && currentUser != null){
                    MessageHelper.createMessageForChat(txtToSend.getText().toString(), currentChatToken, currentUser);
                    txtToSend.setText("");
                }
            }
        });
    }

    // --------------------
    // UI
    // --------------------
    //Configure RecyclerView with a Query
    private void configureRecyclerView(){

        FirestoreRecyclerOptions<Message> options = new FirestoreRecyclerOptions
                .Builder<Message>()
                .setQuery(MessageHelper.getAllMessageForChat(currentChatToken),Message.class)
                .build();

        this.mRecyclerViewAdapter = new MyChatRecyclerViewAdapter(
                options,
                Glide.with(this),
                this,
                this.currentUser.getUid());

        mRecyclerViewAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mRecyclerView.smoothScrollToPosition(mRecyclerViewAdapter.getItemCount()); // Scroll to bottom on new messages
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.setAdapter(this.mRecyclerViewAdapter);
    }

    @Override
    public void onDataChanged() {
        emptyChat.setVisibility(this.mRecyclerViewAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        mRecyclerViewAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mRecyclerViewAdapter.stopListening();
    }
}
