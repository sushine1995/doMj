package wzp.project.majiang.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;

import java.util.List;

import wzp.project.majiang.R;
import wzp.project.majiang.activity.EditChooseCardMethodActivity;
import wzp.project.majiang.activity.EditPlayMethodActivity;
import wzp.project.majiang.adapter.ChooseCardMethodListAdapter;
import wzp.project.majiang.entity.ChooseCardMethod;
import wzp.project.majiang.entity.ChooseCardParameter;


public class ChooseCardMethodFragment extends Fragment {

    private EditPlayMethodActivity activity;

    private Button btnEdit;
    private ListView lvPlayMethod;

    private List<ChooseCardMethod> methodList;
    private ChooseCardMethodListAdapter chooseCardMethodListAdapter;

    private ChooseCardParameter chooseCardParameter;

    public static final int REQUEST_EDIT_CHOOSE_CARD_METHOD = 0x01;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (EditPlayMethodActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_majiang_method, container, false);

        initData();
        initWidget(view);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("ChooseCardMethod", "onActivityResult");
        switch (requestCode) {
            case REQUEST_EDIT_CHOOSE_CARD_METHOD:
                if (resultCode == Activity.RESULT_OK) {
                    int index = data.getIntExtra("index", -1);
                    ChooseCardMethod chooseCardMethod = JSON.parseObject(data.getStringExtra("chooseCardMethod"),
                            ChooseCardMethod.class);
                    chooseCardMethod.setSelected(true);
                    if (index < chooseCardParameter.getMethods().size()) {
                        methodList.set(index, chooseCardMethod);
                    } else {
                        methodList.add(chooseCardMethod);
                    }
                    chooseCardMethodListAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    private void initData() {
        chooseCardParameter = activity.getChooseCardParameter();
        methodList = chooseCardParameter.getMethods();
        chooseCardMethodListAdapter = new ChooseCardMethodListAdapter(getContext(), this, methodList);
    }


    private void initWidget(View view) {
        lvPlayMethod = (ListView) view.findViewById(R.id.lv_play_method);

        lvPlayMethod.setAdapter(chooseCardMethodListAdapter);

        Button btnAddData = (Button) LayoutInflater.from(getContext()).inflate(R.layout.view_add_data, null);
        btnAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (methodList.size() >= 6) {
                    activity.showToast("最多只能包含6条数据！");
                } else {
                    EditChooseCardMethodActivity.myStartActivityForResult(getActivity(), ChooseCardMethodFragment.this,
                            chooseCardParameter.getMethods().size(), REQUEST_EDIT_CHOOSE_CARD_METHOD);
                }
            }
        });
        lvPlayMethod.addFooterView(btnAddData);
    }

}