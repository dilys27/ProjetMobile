package upec.projetandroid20182019.myrequest;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyRequest {

    private Context context;
    private RequestQueue queue;
    private static final String REGISTER_URL = "https://pw.lacl.fr/~u21500989/Projet_Mobile/register.php";
    private static final String LOGIN_URL = "https://pw.lacl.fr/~u21500989/Projet_Mobile/login.php";

    public MyRequest(Context context, RequestQueue queue) {
        this.context = context;
        this.queue = queue;
    }

    public void register(final String pseudo, final String email, final String password, final String password2, final RegisterCallBack callBack){
        StringRequest request = new StringRequest(Request.Method.POST, REGISTER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Map<String, String> errors = new HashMap<>();

                try{
                    JSONObject json = new JSONObject(response);
                    Boolean error = json.getBoolean("error");

                    if(!error){
                        //L'inscription s'est bien déroulée
                        callBack.onSuccess("Vous êtes bien inscrit(e)");
                    }else{
                        JSONObject messages = json.getJSONObject("message");
                        if(messages.has("pseudo")){
                            errors.put("pseudo", messages.getString("pseudo"));
                        }
                        if(messages.has("email")){
                            errors.put("email", messages.getString("email"));
                        }
                        if(messages.has("password")){
                            errors.put("password", messages.getString("password"));
                        }
                        callBack.inputErrors(errors);
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                }
                Log.d("APP", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("APP", "Error = " + error);
                if(error instanceof NetworkError){
                    callBack.onError("Impossible de se connecter");
                }else if(error instanceof VolleyError){
                    callBack.onError("Une erreur est survenue");
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("pseudo", pseudo);
                map.put("email", email);
                map.put("password", password);
                map.put("password2", password2);

                return map;
            }
        };

        queue.add(request);
    }

    public interface RegisterCallBack{
        void onSuccess(String message);
        void inputErrors(Map<String, String> errors);
        void onError(String message);
    }

    public void connection(final String pseudo, final String password, final LoginCallBack callBack){
        StringRequest request = new StringRequest(Request.Method.POST, LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Log.d("APP", response);
                JSONObject json = null;
                try {
                    json = new JSONObject(response);
                    Boolean error = json.getBoolean("error");

                    if(!error){
                        String id = json.getString("id");
                        String pseudo = json.getString("pseudo");
                        callBack.onSuccess(id, pseudo);

                    }else{
                        callBack.onError(json.getString("message"));
                    }

                } catch (JSONException e) {
                    callBack.onError("Une erreur est survenue");
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof NetworkError){
                    callBack.onError("Impossible de se connecter");
                }else if(error instanceof VolleyError){
                    callBack.onError("Une erreur est survenue");
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("pseudo", pseudo);
                map.put("password", password);

                return map;
            }
        };

        queue.add(request);
    }

    public interface LoginCallBack{
        void onSuccess(String id, String pseudo);
        void onError(String message);
    }
}
