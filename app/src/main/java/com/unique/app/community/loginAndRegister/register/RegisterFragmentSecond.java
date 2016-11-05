package com.unique.app.community.loginAndRegister.register;

import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;

import com.unique.app.community.R;
import com.unique.app.community.base.Mvp.BaseFragment;
import com.unique.app.community.base.Mvp.IPresenter;
import com.unique.app.community.base.Mvp.IView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: Alexander
 * Email: yifengtang@hustunique.com
 * Since: 16/10/12.
 */

public class RegisterFragmentSecond extends BaseFragment<RegisterPresenter>
        implements IView {

    @BindView(R.id.reg_second_id_edit_text)
    EditText studentIdEditText;
    @BindView(R.id.reg_second_nick_edit_text)
    EditText nickNameEdittext;
    @BindView(R.id.reg_button)
    Button buttonLogin;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register_second;
    }

    @Override
    protected void initEventAndData() {
        initializeTexts();
    }

    @Override
    protected RegisterPresenter getPresenter() {
        return ((RegisterActivity)getActivity()).getMPresenter();
    }



    /**
     *  Initialize all listeners
     */

    @OnClick(R.id.reg_button)
    void finishRegister(){
        String sId = studentIdEditText.getText().toString().trim();
        String nickname = nickNameEdittext.getText().toString().trim();

        //TODO:check sId
        mPresenter.register(sId,nickname);
    }

    /**
     *  Initialize all hints and texts
     */

    private void initializeTexts(){
        studentIdEditText.setHint(getResources().getString(R.string.student_id));
        nickNameEdittext.setHint(getResources().getString(R.string.nickname));
        buttonLogin.setText(getResources().getString(R.string.finish_register));
    }
}
