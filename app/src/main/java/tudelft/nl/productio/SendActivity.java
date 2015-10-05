package tudelft.nl.productio;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by rik on 30/09/15.
 */
public class SendActivity extends Activity {

    @Bind(R.id.barcode_text)
    TextView mBarcodeText;
    private Barcode mBarcode;
    private ProductIOService mProductIOService;
    private String mUser = "Rik";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        ButterKnife.bind(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://erwinvaneyk.nl")
                .build();
        mProductIOService = retrofit.create(ProductIOService.class);

        mBarcode = (Barcode)getIntent().getExtras().get("barcode");
        mBarcodeText.setText("Barcode "+mBarcode.rawValue);
    }

    @OnClick(R.id.delete)
    public void deleteProduct(View view) {
        Call<Void> call = mProductIOService.removeProduct(mUser, mBarcode.rawValue);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Response<Void> response, Retrofit retrofit) {
                System.out.println(response.body());
                try {
                    System.out.println(response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @OnClick(R.id.add)
    public void addProduct(View view) {
        Call<Void> call = mProductIOService.addProduct(mUser, mBarcode.rawValue);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Response<Void> response, Retrofit retrofit) {
                System.out.println(response.body());
                try {
                    System.out.println(response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
