// Generated by view binder compiler. Do not edit!
package com.example.myreadingapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.myreadingapp.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityLoginBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnLogin;

  @NonNull
  public final Button btnSignUp;

  @NonNull
  public final EditText editTextText;

  @NonNull
  public final EditText editTextTextPassword;

  @NonNull
  public final ImageView imageView2;

  @NonNull
  public final ConstraintLayout main;

  @NonNull
  public final TextView txtForgotPassword;

  private ActivityLoginBinding(@NonNull ConstraintLayout rootView, @NonNull Button btnLogin,
      @NonNull Button btnSignUp, @NonNull EditText editTextText,
      @NonNull EditText editTextTextPassword, @NonNull ImageView imageView2,
      @NonNull ConstraintLayout main, @NonNull TextView txtForgotPassword) {
    this.rootView = rootView;
    this.btnLogin = btnLogin;
    this.btnSignUp = btnSignUp;
    this.editTextText = editTextText;
    this.editTextTextPassword = editTextTextPassword;
    this.imageView2 = imageView2;
    this.main = main;
    this.txtForgotPassword = txtForgotPassword;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityLoginBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityLoginBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_login, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityLoginBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_login;
      Button btnLogin = ViewBindings.findChildViewById(rootView, id);
      if (btnLogin == null) {
        break missingId;
      }

      id = R.id.btn_sign_up;
      Button btnSignUp = ViewBindings.findChildViewById(rootView, id);
      if (btnSignUp == null) {
        break missingId;
      }

      id = R.id.editTextText;
      EditText editTextText = ViewBindings.findChildViewById(rootView, id);
      if (editTextText == null) {
        break missingId;
      }

      id = R.id.editTextTextPassword;
      EditText editTextTextPassword = ViewBindings.findChildViewById(rootView, id);
      if (editTextTextPassword == null) {
        break missingId;
      }

      id = R.id.imageView2;
      ImageView imageView2 = ViewBindings.findChildViewById(rootView, id);
      if (imageView2 == null) {
        break missingId;
      }

      ConstraintLayout main = (ConstraintLayout) rootView;

      id = R.id.txt_forgot_password;
      TextView txtForgotPassword = ViewBindings.findChildViewById(rootView, id);
      if (txtForgotPassword == null) {
        break missingId;
      }

      return new ActivityLoginBinding((ConstraintLayout) rootView, btnLogin, btnSignUp,
          editTextText, editTextTextPassword, imageView2, main, txtForgotPassword);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
