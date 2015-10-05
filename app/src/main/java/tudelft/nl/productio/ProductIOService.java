package tudelft.nl.productio;

import retrofit.Call;
import retrofit.http.DELETE;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by rik on 30/09/15.
 */
public interface ProductIOService {
    @POST("/api/product/{id}")
    Call<Void> addProduct(@Header("user") String user, @Path("id") String barcodeValue);

    @DELETE("/api/product/{id}")
    Call<Void> removeProduct(@Header("user") String user, @Path("id") String barcodeValue);
}
