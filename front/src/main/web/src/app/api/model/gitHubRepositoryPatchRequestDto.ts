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
import { RepositoryPatchRequestDto } from './repositoryPatchRequestDto';

/**
 * Request asking the update of a GitHub repository
 */
export interface GitHubRepositoryPatchRequestDto extends RepositoryPatchRequestDto { 
    /**
     * The name of the default branch used to find translations
     */
    defaultBranch: string;
    /**
     * Regex specifying branches that can be scanned by this tool.
     */
    allowedBranches: string;
    /**
     * Access key to use to access this repository (empty means no access key)
     */
    accessKey?: string;
    /**
     * Access key to use to access this repository (empty means no password)
     */
    webHookSecret?: string;
}
export namespace GitHubRepositoryPatchRequestDto {
}
