package com.example.project;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import android.util.Log;

public class ApiClient {
    private static final String BASE_URL = "http://192.168.0.18:5000"; // Используйте ваш IPv4-адрес
    private RequestQueue requestQueue;

    public ApiClient(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public void registerUser(String username, String hashedPassword, String saltHex, String phone, String email, final ApiCallback callback) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username);
            jsonBody.put("hashedPassword", hashedPassword);
            jsonBody.put("saltHex", saltHex);
            jsonBody.put("phone", phone);
            jsonBody.put("email", email);
        } catch (Exception e) {
            e.printStackTrace();
            callback.onError(new VolleyError("JSON error", e));
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASE_URL + "/register", jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ApiClient", "Registration success: " + response.toString());
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ApiClient", "Registration error: " + (error != null ? error.toString() : "null"));
                        if (error != null) {
                            error.printStackTrace();
                            if (error.networkResponse != null) {
                                Log.e("ApiClient", "Error status code: " + error.networkResponse.statusCode);
                                Log.e("ApiClient", "Error data: " + new String(error.networkResponse.data));
                            }
                        }
                        callback.onError(error);
                    }
                });

        requestQueue.add(request);
    }

    public void loginUser(String username, String password, final ApiCallback callback) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username);
            jsonBody.put("password", password);
        } catch (Exception e) {
            e.printStackTrace();
            callback.onError(new VolleyError("JSON error", e));
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASE_URL + "/login", jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ApiClient", "Login success: " + response.toString());
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ApiClient", "Login error: " + (error != null ? error.toString() : "null"));
                        if (error != null) {
                            error.printStackTrace();
                            if (error.networkResponse != null) {
                                Log.e("ApiClient", "Error status code: " + error.networkResponse.statusCode);
                                Log.e("ApiClient", "Error data: " + new String(error.networkResponse.data));
                            }
                        }
                        callback.onError(error);
                    }
                });

        requestQueue.add(request);
    }

    public void verifyTwoFactorCode(String sessionId, String code, final ApiCallback callback) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("sessionId", sessionId);
            jsonBody.put("code", code);
        } catch (Exception e) {
            e.printStackTrace();
            callback.onError(new VolleyError("JSON error", e));
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASE_URL + "/verifyTwoFactor", jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ApiClient", "Two-factor verification success: " + response.toString());
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ApiClient", "Two-factor verification error: " + (error != null ? error.toString() : "null"));
                        if (error != null) {
                            error.printStackTrace();
                            if (error.networkResponse != null) {
                                Log.e("ApiClient", "Error status code: " + error.networkResponse.statusCode);
                                Log.e("ApiClient", "Error data: " + new String(error.networkResponse.data));
                            }
                        }
                        callback.onError(error);
                    }
                });

        requestQueue.add(request);
    }

    public interface ApiCallback {
        void onSuccess(JSONObject response);
        void onError(VolleyError error);
    }
}