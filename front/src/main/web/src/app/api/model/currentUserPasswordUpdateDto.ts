/**
 * i18n Tool
 * Web API of the i18n tool
 *
 * OpenAPI spec version: 1.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

/**
 * The update of the current user password.
 */
export interface CurrentUserPasswordUpdateDto { 
    /**
     * The current user's password.
     */
    currentPassword?: string;
    /**
     * The new user's password.
     */
    newPassword?: string;
}