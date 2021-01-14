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
import { RepositoryRolesDto } from './repositoryRolesDto';

/**
 * Description of an authenticated user.
 */
export interface AuthenticatedUserDto { 
    /**
     * Unique id of the authenticated user.
     */
    id?: string;
    /**
     * Description of the user.
     */
    userId?: string;
    /**
     * Roles allowed during this session.
     */
    sessionRoles?: Array<AuthenticatedUserDto.SessionRolesDtoEnum>;
    /**
     * All the current repository roles.
     */
    repositoryRoles?: Array<RepositoryRolesDto>;
}
export namespace AuthenticatedUserDto {
    export type SessionRolesDtoEnum = 'MEMBER_OF_ORGANIZATION' | 'MEMBER_OF_REPOSITORY' | 'ADMIN';
    export const SessionRolesDtoEnum = {
        MEMBEROFORGANIZATION: 'MEMBER_OF_ORGANIZATION' as SessionRolesDtoEnum,
        MEMBEROFREPOSITORY: 'MEMBER_OF_REPOSITORY' as SessionRolesDtoEnum,
        ADMIN: 'ADMIN' as SessionRolesDtoEnum
    };
}