package com.specterex.permission;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.specterex.domain.auth.Permission;
import com.specterex.domain.auth.RolePermissionMapping;
import com.specterex.enums.DefaultRole;
/**
 * 
 * Permission manager of application.
 * If new APIs are created then their permission should be add in  
 * map based on their role for successful API access
 *
 */
public class PermissionAssigner {
	private static Logger LOGGER = LoggerFactory.getLogger(PermissionAssigner.class);
	private static final Set<RolePermissionMapping> PERMISSION;
	private static Set<Permission> userPermission = new HashSet<Permission>();
	private static Set<Permission> delegateAdminPermission = new HashSet<Permission>();
	private static Set<Permission> superAdminPermission = new HashSet<Permission>();
	static {
		LOGGER.info("Permission assigner is executed");
		Set<RolePermissionMapping> mainPermission = new HashSet<RolePermissionMapping>();
		/*************** User permission *********************/

		userPermission.add(new Permission("PERM_LOGOUT", "Logout user","PERM_LOGOUT"));		
//		userPermission.add(new Permission("PERM_LOGGEDIN_USER", "Get loggedin user","USER_BASIC_PERM"));
		userPermission.add(new Permission("PERM_UPDATE_PASSWORD", "Update password","PERM_UPDATE_PASSWORD"));	
		userPermission.add(new Permission("PERM_USER_UPDATE", "Update user","USER_BASIC_PERM"));	
		userPermission.add(new Permission("PERM_GET_USER_DETAILS", "User_Detail","USER_BASIC_PERM"));
		userPermission.add(new Permission("PERM_SEND_VERIFICATION_OTP", "Send OTP for verification","USER_BASIC_PERM"));
		userPermission.add(new Permission("PERM_UPLOAD_DOCUMENT", "Upload document","PERM_UPLOAD_DOCUMENT"));
	
		userPermission.add(new Permission("PERM_2FA_VIA_GOOGLE_AUTH", "Get two factor auth key","USER_2FA_PERM"));
		userPermission.add(new Permission("PERM_2FA_VIA_GOOGLE_AUTH_VERIFY", "Verify google auth key and set 2FA via google auth","USER_2FA_PERM"));
		userPermission.add(new Permission("PERM_BITCOIN_BALANCE", "Get loggedin user bitcoin wallet balance","USER_2FA_PERM"));
		userPermission.add(new Permission("PERM_BITCOIN_WALLET_ADDRESS", "Get loggedin user bitcoin wallet balance","USER_2FA_PERM"));
		userPermission.add(new Permission("PERM_2FA_VIA_SMS_AUTH_VERIFY", "verify OTP and set 2FA via SMS","USER_2FA_PERM"));
		userPermission.add(new Permission("PERM_REMOVE_2FA", "Remove 2FA","USER_2FA_PERM"));
		userPermission.add(new Permission("PERM_TRANSFER_BTC", "Transfer bitcoins","PERM_TRANSFER_BTC"));
		userPermission.add(new Permission("PERM_BITCOIN_CONFIRMATION", "Transfer Confirmations","PERM_BITCOIN_CONFIRMATION"));
		userPermission.add(new Permission("PERM_TRANSFER_BTC_LIST", "Transfer bitcoins list","PERM_TRANSFER_BTC_LIST"));
		userPermission.add(new Permission("PERM_POST_TRADE", "post a trade","PERM_POST_TRADE"));
		userPermission.add(new Permission("PERM_POST_TRADE_LIST", "List of trade posted","PERM_POST_TRADE_LIST"));
		userPermission.add(new Permission("PERM_BUY_TRADE", "Buy a trade","PERM_BUY_TRADE"));
		userPermission.add(new Permission("PERM_SELL_TRADE", "Sell a trade","PERM_SELL_TRADE"));
		userPermission.add(new Permission("PERM_MKT_LIVE_PRICE", "Live price of cryotoCurrency","PERM_MKT_LIVE_PRICE"));
      userPermission.add(new Permission("PERM_FIAT_LIVE_PRICE", "Live price of fiatCurrency","PERM_FIAT_LIVE_PRICE"));
      userPermission.add(new Permission("PERM_BUYER_CANCEL", "Cancel trade by buyer","PERM_BUYER_CANCEL"));
      userPermission.add(new Permission("PERM_BUYER_PAYMENT_TRANSFERRED", "Buyer payment transferred","PERM_BUYER_PAYMENT_TRANSFERRED"));
      userPermission.add(new Permission("PERM_SELLER_PAYMENT_RECEIVED", "Seller payment received confirmation","PERM_SELLER_PAYMENT_RECEIVED"));
      
      
		mainPermission.add(new RolePermissionMapping(DefaultRole.ROLE_USER.toString(), userPermission));
		/************** Delegate admin permission *************/
	
		mainPermission.add(new RolePermissionMapping(DefaultRole.ROLE_DELEGATE_ADMIN.toString(), delegateAdminPermission));
		
		/************** Super admin permission ****************/
	
		superAdminPermission.add(new Permission("PERM_GET_USER_DETAILS_ADMIN","User_Detail","PERMISSION"));
		superAdminPermission.add(new Permission("PERM_UPDATE_USER_KYC","Update KYC","PERMISSION"));
		superAdminPermission.add(new Permission("PERM_GET_USER_DETAILS_ADMIN_VERIFICATION","User_Detail","PERMISSION"));
		superAdminPermission.add(new Permission("PERM_GET_ALL_USER","User_List","PERMISSION"));
		superAdminPermission.add(new Permission("PERM_ESCROW_BITCOIN_BALANCE","Balance","PERMISSION"));
		mainPermission.add(new RolePermissionMapping(DefaultRole.ROLE_SUPER_ADMIN.toString(), superAdminPermission));
		PERMISSION = Collections.unmodifiableSet(mainPermission);
	}
	/**
	 * Get all permissions
	 * @return Map
	 */
	public static Set<RolePermissionMapping> getAllMappedPermissions() {
		return PERMISSION;
	}
	
	public static Set<Permission> getUserPermissions(){
		return userPermission;
	}
	
	public static Set<Permission> getDelegateAdminPermissions(){
		return delegateAdminPermission;
	}
	public static Set<Permission> getSuperAdminPermissions(){
		return superAdminPermission;
	}
}
