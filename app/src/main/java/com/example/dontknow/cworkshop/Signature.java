package com.example.dontknow.cworkshop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Signature extends AppCompatActivity {

    private SignaturePad signaturePad;

    private Uri ImageUri= null;

    private Button clear;
    private Button online;
    private EditText Remaining;

    public static final int Request_Code = 1243;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 12345;

    private Bundle bundle;

    private StorageReference storageReference;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReferenceCWEntry;
    private DatabaseReference databaseReferenceCWCount;

    private ProgressDialog progressDialog;

    private boolean done=false;
    private int cnt;
    private String OfflineImage;

    private MydbHandler mydbHandler;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Request_Code)
        {
                Toast.makeText(this,"Mail is sent to Participant!!!",Toast.LENGTH_LONG).show();
                Intent intent1 =new Intent(Signature.this,MainActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        clear = (Button)findViewById(R.id.idclear);
        online = (Button)findViewById(R.id.idOnlineEntry);
        Remaining =(EditText) findViewById(R.id.idRemaining);

        storageReference = FirebaseStorage.getInstance().getReference();

        signaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        bundle = getIntent().getExtras();

        databaseReferenceCWEntry= FirebaseDatabase.getInstance().getReference().child("CWEntry");

        databaseReferenceCWCount = FirebaseDatabase.getInstance().getReference().child("CWCount");

        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(Remaining.getText()))
                {
                    Toast.makeText(getApplicationContext(),"Please Enter Remaining Fees If Any Otherwise Enter 0 In That Field",Toast.LENGTH_LONG).show();
                }
                else
                {
                    progressDialog.setMessage("Saving Signature!!!");
                    progressDialog.show();
                    createSignature();
                    progressDialog.setMessage("Checking Connection");
                    ConnectivityManager connec = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo networkInfo  = connec.getActiveNetworkInfo();
                    if(networkInfo!=null && networkInfo.isConnected())
                    {
                        //Toast.makeText(getApplicationContext(),"You Are Connected To Server",Toast.LENGTH_LONG).show();
                        progressDialog.setMessage("Saving Entry To Online Database!!!!");


                        //Toast.makeText(getApplicationContext(),""+dataSnapshot.getValue(),Toast.LENGTH_LONG).show();
                                //Toast.makeText(getApplicationContext(),""+dataSnapshot.getValue(),Toast.LENGTH_LONG).show();

                                cnt++;
                        final DatabaseReference newEntry = databaseReferenceCWEntry.push();
                                final String id =newEntry.getKey();

                                if(ImageUri!=null)
                                {
                                    StorageReference filepath = storageReference.child("SignatureImages").child(id);
                                    filepath.putFile(ImageUri).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            databaseReferenceCWEntry.child(id).removeValue();
                                            Toast.makeText(Signature.this,"Failed To Upload Image To Database",Toast.LENGTH_LONG).show();
                                            e.printStackTrace();
                                        }
                                    });

                                    filepath.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                            Uri downloadUrl = taskSnapshot.getDownloadUrl();


                                            newEntry.child("ID").setValue(id);
                                            newEntry.child("FirstName").setValue(bundle.getString("FirstName"));
                                            newEntry.child("MidName").setValue(bundle.getString("MidName"));
                                            newEntry.child("LastName").setValue(bundle.getString("LastName"));
                                            newEntry.child("Email").setValue(bundle.getString("Email"));
                                            newEntry.child("Phone").setValue(bundle.getString("Phone"));
                                            newEntry.child("College").setValue(bundle.getString("College"));
                                            newEntry.child("Year").setValue(bundle.getString("Year"));
                                            newEntry.child("Remaining").setValue(""+Remaining.getText().toString());
                                            newEntry.child("Signature").setValue(downloadUrl.toString().trim());
                                            newEntry.child("Date").setValue(""+System.currentTimeMillis());
                                            newEntry.child("Session1").setValue("Absent");
                                            newEntry.child("Session2").setValue("Absent");
                                            newEntry.child("Session3").setValue("Absent");
                                            newEntry.child("Session4").setValue("Absent");
                                            DatabaseReference data=FirebaseDatabase.getInstance().getReference().child("Users");
                                            data.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if(dataSnapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()))
                                                    {
                                                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                                                        String name = ""+dataSnapshot.child(uid).child("First_Name").getValue().toString()+" "+dataSnapshot.child(uid).child("Last_Name").getValue().toString()+" "+dataSnapshot.child(uid).child("Post_Of_User").getValue().toString();
                                                        newEntry.child("userName").setValue(name);
                                                        newEntry.child("userUID").setValue(uid);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }

                                            });

                                            //databaseReferenceCWCount.setValue(""+cnt);


                                                   /* Intent intent =new Intent(Intent.ACTION_SEND);
                                                    intent.setData(Uri.parse("To:"));
                                                    intent.putExtra(Intent.EXTRA_EMAIL,new String[]{bundle.getString("Email")});
                                                    intent.putExtra(Intent.EXTRA_SUBJECT,"Congratulations!! Your are successfully registered for C Workshop.");
                                                    intent.putExtra(Intent.EXTRA_TEXT,"Thank You "+bundle.getString("FirstName")+" "+bundle.getString("MidName")+" "+bundle.getString("LastName")+" \n"+"Your registration id is :"+id+" Please" +
                                                            "Keep this id with you."+" It is needed"+" For any Doubts Call On This Number "+"Vinit Mahajan : 9421426366");
                                                    intent.setType("message/rfc822");
                                                    setResult(RESULT_OK,intent);
                                                    startActivityForResult(Intent.createChooser(intent,"SEND EMAIL VIA:"),Request_Code);
                                                    */
                                            String url = "https://mahajanvv.000webhostapp.com/send-mail.php";
                                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    Toast.makeText(getApplicationContext(),"mail is sent",Toast.LENGTH_LONG).show();
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(getApplicationContext(),"mail is not sent"+error.toString(),Toast.LENGTH_LONG).show();
                                                }
                                            }){
                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String,String>params = new HashMap<>();
                                                    params.put("email",bundle.getString("Email"));
                                                    params.put("id",id);
                                                    params.put("firstname",bundle.getString("FirstName"));
                                                    params.put("lastname",bundle.getString("LastName"));
                                                    return params;
                                                }
                                            };
                                            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
                                            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                                    5000,
                                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                                            String wayurl="https://mahajanvv.000webhostapp.com/Way2SMS-API-master/sendsms.php";
                                            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, wayurl, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    Toast.makeText(getApplicationContext(),"message is sent",Toast.LENGTH_LONG).show();
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(getApplicationContext(),"message is not sent"+error.toString(),Toast.LENGTH_LONG).show();
                                                }
                                            }){
                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String,String>params = new HashMap<>();
                                                    params.put("to",bundle.getString("Phone"));
                                                    params.put("sub","Thank You!\n"+" "+bundle.getString("FirstName")+" "+bundle.getString("LastName")+" You are successfully registered for CWorkshop ."+" . Workshop is on 3rd and 9th September @ 9:00 AM."
                                                            +"\n\n\n\n\n\n\n\n");
                                                    return params;
                                                }
                                            };
                                            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest1);
                                            stringRequest1.setRetryPolicy(new DefaultRetryPolicy(
                                                    5000,
                                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                            Intent intent = new Intent(Signature.this,MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);

                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(),"SuccessFully Saved To Online DataBase!!!",Toast.LENGTH_LONG).show();
                                            /**/

                                        }
                                    });

                                }
                                else {
                                    databaseReferenceCWEntry.child(id).removeValue();
                                    progressDialog.dismiss();
                                    Toast.makeText(Signature.this, "ImageUri is null", Toast.LENGTH_LONG).show();
                                }


                    }
                    else
                    {
                        // Toast.makeText(getApplicationContext(),"Not Connected",Toast.LENGTH_LONG).show();
                        progressDialog.setMessage("Saving Entry to Offline Database!!!");
                        mydbHandler.add(new CWDatabase(bundle.getString("FirstName"),bundle.getString("MidName"),bundle.getString("LastName"),bundle.getString("Email"),bundle.getString("Phone"),bundle.getString("College"),bundle.getString("Year"),OfflineImage,Remaining.getText().toString(),"Not Uploaded"));
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"SuccessFully Saved To Offline Database!!!",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Signature.this,MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signaturePad.clear();
            }
        });

        if(checkPermissionREAD_EXTERNAL_STORAGE(this))
        {}

        mydbHandler = new MydbHandler(this,null,null,1);
    }

    private void createSignature() {

        Bitmap signatureBitmap = signaturePad.getSignatureBitmap();
        if(addJpgSignatureToGallery(signatureBitmap)){
            Toast.makeText(this, "JPG format saved into Gallery", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Failed to save jpg format to Gallery", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean addJpgSignatureToGallery(Bitmap signature) {
            boolean result = false;
            File photo = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));;
            //Toast.makeText(Signature.this,"1",Toast.LENGTH_LONG).show();
            saveBitmapToJPG(signature, photo);
        //Toast.makeText(Signature.this,"2",Toast.LENGTH_LONG).show();

        scanMediaFile(photo);

        OfflineImage = photo.getAbsolutePath();

        ImageUri = Uri.fromFile(photo);
            result = true;
            return result;
        }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        Signature.this.sendBroadcast(mediaScanIntent);
    }

    private File getAlbumStorageDir(String albumName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);

        if(!file.mkdirs()){
            Log.d("TAG", "Directory not created");
        }

        return file;
    }

    private void saveBitmapToJPG(Bitmap signature, File photo) {
        Bitmap newBitmap = Bitmap.createBitmap(signature.getWidth(), signature.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(signature, 0,0, null);
        try {
            OutputStream stream = new FileOutputStream(photo);
            newBitmap.compress(Bitmap.CompressFormat.JPEG,80,stream);
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                } else {
                    //Toast.makeText(Login.this, "GET_ACCOUNTS Denied",
                    // Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }
    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }
    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { android.Manifest.permission.READ_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }
}
