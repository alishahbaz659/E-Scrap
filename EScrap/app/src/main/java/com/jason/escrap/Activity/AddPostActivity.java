package com.jason.escrap.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.jason.escrap.Database.AddPosts;
import com.jason.escrap.Model.Alert;
import com.jason.escrap.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddPostActivity extends AppCompatActivity {
    @BindView(R.id.checkbox_select_single)
    CheckBox check_single_box;
    @BindView(R.id.checkbox_select_multiple)
    CheckBox check_multi_box;
    @BindView(R.id.img_select_image)
    ImageView select_image_gallery;
    @BindView(R.id.selected_image_count)
    TextView select_image_count;
    @BindView(R.id.edt_caption)
    EditText edt_caption;
    @BindView(R.id.edt_productType)
    EditText edt_productType;
    @BindView(R.id.edt__manufacturingdate)
    EditText edt_manufacturingdate;
    @BindView(R.id.edt_expirydate)
    EditText edt_expirydate;
    @BindView(R.id.edt_brief)
    EditText edt_brief;
    @BindView(R.id.checkbox_availability_lend)
    CheckBox checkBox_lend_availabality;
    @BindView(R.id.lending_price)
    EditText lending_price;
    @BindView(R.id.checkbox_availability_sale)
    CheckBox checkBox_sale_availability;
    @BindView(R.id.edt_sale_price)
    EditText adoption_price;
    @BindView(R.id.btn_post)
    Button btn_post;
    @BindView(R.id.radio_yes)
    RadioButton radio_yes;
    @BindView(R.id.radio_no)
    RadioButton radio_no;
    @BindView(R.id.l3)
    LinearLayout l3;
    @BindView(R.id.l4)
    LinearLayout l4;
    public Uri imagepath;
    Alert alert;
    String genetic_makeup_val, sr_brief_description;
    String dog_type, caption, product_type, manufacturing_date, expiry_date,
            brief_history, warranty, lending_availability, lending_availability_price, sale_availability, sale_availability_price;
    AddPosts addPosts;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        ButterKnife.bind(this);
        alert = new Alert(this);
        l3.setVisibility(View.GONE);
        l4.setVisibility(View.GONE);
        firebaseAuth = FirebaseAuth.getInstance();
        addPosts = new AddPosts(this);
        checkBox_lend_availabality.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    l3.setVisibility(View.VISIBLE);
                } else {
                    l3.setVisibility(View.GONE);
                }
            }
        });
        checkBox_sale_availability.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    l4.setVisibility(View.VISIBLE);
                } else {
                    l4.setVisibility(View.GONE);
                }
            }
        });
        select_image_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 123);
            }
        });
        btn_post.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                if (check_single_box.isChecked()) {
                    dog_type = "Single";
                } else if (check_multi_box.isChecked()) {
                    dog_type = "Multi";
                }
                caption = edt_caption.getText().toString();
                product_type = edt_productType.getText().toString();
                manufacturing_date = edt_manufacturingdate.getText().toString();
                expiry_date = edt_expirydate.getText().toString();
                brief_history = edt_brief.getText().toString();
                if (radio_yes.isChecked()) {
                    warranty = "yes";
                } else if (radio_no.isChecked()) {
                    warranty = "no";
                }
                if (checkBox_lend_availabality.isChecked()) {
                    lending_availability = "Yes";
                } else {
                    lending_availability = "No";
                }
                lending_availability_price = lending_price.getText().toString();
                if (checkBox_sale_availability.isChecked()) {
                    sale_availability = "Yes";
                } else {
                    sale_availability = "No";
                }
                sale_availability_price = adoption_price.getText().toString();

                if (isValid(check_single_box, check_multi_box, select_image_gallery, caption, product_type, manufacturing_date, expiry_date, brief_history, checkBox_lend_availabality,
                        lending_availability_price, checkBox_sale_availability, sale_availability_price)) {
                    addPosts.upload_new_post(dog_type,caption,product_type,manufacturing_date,expiry_date,brief_history,
                            warranty,lending_availability,lending_availability_price,sale_availability,sale_availability_price,
                            imagepath,firebaseAuth.getUid(),firebaseAuth);
                }
            }
        });

    }


    public boolean isValid(CheckBox check_single_box, CheckBox check_multi_box, ImageView img_gallery, String caption,
                           String product_type, String manufacturing_date, String expiry_date, String brief_description,
                           CheckBox checkBox_lend_availabality, String lend_price,
                           CheckBox checkBox_sale_availability, String sale_price
    ) {
        if (!check_single_box.isChecked() && !check_multi_box.isChecked()) {
            alert.error_alert("Error", "Please choose type");
            return false;
        } else if (caption.isEmpty() || product_type.isEmpty() || manufacturing_date.isEmpty()) {
            alert.error_alert("Error", "Please enter all details");
            return false;
        } else if (imagepath==null) {
            alert.error_alert("Error", "Please choose image from gallery");
            return false;
        } else if (!checkBox_lend_availabality.isChecked() && !checkBox_sale_availability.isChecked()) {
            alert.error_alert("Error", "Please select at-least one availability");
            return false;
        } else if (checkBox_lend_availabality.isChecked() && lend_price.isEmpty()) {
            alert.error_alert("Error", "Please enter breed price");
            return false;
        } else if (checkBox_sale_availability.isChecked() && sale_price.isEmpty()) {
            alert.error_alert("Error", "Please enter adoption price");
            return false;
        } else if (expiry_date.isEmpty()) {
            genetic_makeup_val = "";
            return true;
        } else if (brief_description.isEmpty()) {
            sr_brief_description = "";
            return true;
        }
        return true;
    }


    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                imagepath = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imagepath);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                select_image_gallery.setImageBitmap(selectedImage);
                select_image_count.setText("One image selected");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}