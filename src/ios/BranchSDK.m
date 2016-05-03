/********* BranchSDK.m Cordova Plugin Implementation *******/

#import <Cordova/CDV.h>
#import <Branch/Branch.h>

@interface BranchSDK : CDVPlugin {
    // Member variables go here.
}

- (void)initSession:(CDVInvokedUrlCommand*)command;
- (void)getLatestReferringParams:(CDVInvokedUrlCommand*)command;
- (void)getFirstReferringParams:(CDVInvokedUrlCommand*)command;
- (void)setIdentity:(CDVInvokedUrlCommand*)command;
- (void)userCompletedAction:(CDVInvokedUrlCommand*)command;

@end

@implementation BranchSDK

- (void)initSession:(CDVInvokedUrlCommand*)command
{
    [self.commandDelegate runInBackground:^{
        [[Branch getInstance] initSessionWithLaunchOptions:nil andRegisterDeepLinkHandler:^(NSDictionary *params, NSError *error) {
            CDVPluginResult* pluginResult = nil;
            if (!error) {
                pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:params];
            } else {
                pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
            }
            [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
        }];
    }];
}

-(void)getFirstReferringParams:(CDVInvokedUrlCommand *)command {
    [self.commandDelegate runInBackground:^{
        CDVPluginResult* pluginResult = nil;
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:[[Branch getInstance] getFirstReferringParams]];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

-(void)getLatestReferringParams:(CDVInvokedUrlCommand *)command {
    [self.commandDelegate runInBackground:^{
        CDVPluginResult* pluginResult = nil;
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:[[Branch getInstance] getLatestReferringParams]];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

-(void)setIdentity:(CDVInvokedUrlCommand *)command {
    [self.commandDelegate runInBackground:^{
        [[Branch getInstance] setIdentity:[command.arguments objectAtIndex:0] withCallback:^(NSDictionary *params, NSError *error) {
            CDVPluginResult* pluginResult = nil;
            if (!error) {
                pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:params];
            }
            else {
                pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:[error localizedDescription]];
            }
            [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
        }];
    }];
}

-(void)userCompletedAction:(CDVInvokedUrlCommand *)command {
    [self.commandDelegate runInBackground:^{
        NSString *name;
        NSDictionary *state;
        // if a state dictionary is passed as an argument
        if ([command.arguments count] == 2) {
            name = [command.arguments objectAtIndex:0];
            state = [command.arguments objectAtIndex:1];
        }
        else {
            name = (NSString *)command.arguments;
        }
        
        if (state) {
            [[Branch getInstance] userCompletedAction:name withState:state];
        }
        else {
            [[Branch getInstance] userCompletedAction:name];
        }
        CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

@end
