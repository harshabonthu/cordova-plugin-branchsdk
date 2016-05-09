package io.branch;

import android.content.Intent;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;

public class BranchSDK extends CordovaPlugin {

    @Override
    public void onNewIntent(Intent intent) {
        this.cordova.getActivity().setIntent(intent);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("initSession")) {
            this.initSession(callbackContext);
            return true;
        } else if (action.equals("getLatestReferringParams")) {
            this.getLatestReferringParams(callbackContext);
            return true;
        } else if (action.equals("getFirstReferringParams")) {
            this.getFirstReferringParams(callbackContext);
            return true;
        } else if (action.equals("userCompletedAction")) {
            if (args.length() == 2) {
                this.userCompletedAction(args.getString(0), args.getJSONObject(1), callbackContext);
            } else if (args.length() == 1) {
                this.userCompletedAction(args.getString(0), callbackContext);
            }
            return true;
        } else if (action.equals("setIdentity")) {
            this.setIdentity(args.getString(0), callbackContext);
            return true;
        }
        return false;
    }

    private void setIdentity(final String identity, final CallbackContext callbackContext) {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                Branch.getAutoInstance(cordova.getActivity().getApplicationContext()).setIdentity(identity);
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
            }
        });
    }

    private void userCompletedAction(final String action, final CallbackContext callbackContext) {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                Branch.getAutoInstance(cordova.getActivity().getApplicationContext()).userCompletedAction(action);
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
            }
        });
    }

    private void userCompletedAction(final String action, final JSONObject metaData, final CallbackContext callbackContext) {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                Branch.getAutoInstance(cordova.getActivity().getApplicationContext()).userCompletedAction(action, metaData);
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
            }
        });
    }

    private void getFirstReferringParams(final CallbackContext callbackContext) {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                JSONObject installParams = Branch.getAutoInstance(cordova.getActivity().getApplicationContext()).getFirstReferringParams();
                callbackContext.success(installParams);
            }
        });
    }

    private void getLatestReferringParams(final CallbackContext callbackContext) {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                JSONObject sessionParams = Branch.getAutoInstance(cordova.getActivity().getApplicationContext()).getLatestReferringParams();
                callbackContext.success(sessionParams);
            }
        });
    }

    private void initSession(final CallbackContext callbackContext) {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                Branch branch = Branch.getAutoInstance(cordova.getActivity().getApplicationContext());
                branch.initSession(new Branch.BranchReferralInitListener() {
                    @Override
                    public void onInitFinished(JSONObject referringParams, BranchError error) {
                        if (error == null) {
                            callbackContext.success(referringParams);
                        } else {
                            callbackContext.error(error.getMessage());
                        }
                    }
                }, cordova.getActivity().getIntent().getData(), cordova.getActivity());
            }
        });

    }
}
