package com.example.project;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import org.json.JSONException;
import android.util.Log;

public class ApiClient {
    private static final String BASE_URL = "http://192.168.186.12:5000";
    private final RequestQueue requestQueue;

    public ApiClient(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public void registerUser(String username, String password, String email, final ApiCallback callback) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username);
            jsonBody.put("password", password);
            jsonBody.put("email", email);
        } catch (JSONException e) {
            callback.onError(new VolleyError("JSON error", e));
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                BASE_URL + "/register",
                jsonBody,
                response -> {
                    Log.d("ApiClient", "Registration success");
                    callback.onSuccess(response);
                },
                error -> {
                    Log.e("ApiClient", "Registration error: " + error.toString());
                    if (error.networkResponse != null) {
                        Log.e("ApiClient", "Status code: " + error.networkResponse.statusCode);
                    }
                    callback.onError(error);
                }
        );

        requestQueue.add(request);
    }

    public void loginUser(String username, String password, final ApiCallback callback) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            callback.onError(new VolleyError("JSON error", e));
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                BASE_URL + "/login",
                jsonBody,
                response -> {
                    Log.d("ApiClient", "Login success");
                    callback.onSuccess(response);
                },
                error -> {
                    Log.e("ApiClient", "Login error: " + error.toString());
                    callback.onError(error);
                }
        );

        requestQueue.add(request);
    }

    public interface ApiCallback {
        void onSuccess(JSONObject response);
        void onError(VolleyError error);
    }
}