import {UserRole} from "./user-role.model";
import {UserDto} from "../../../api";

export const ADMIN_USERNAME = 'admin';

export class User {

    public static fromDto(dto: UserDto): User {
        return new User(
            dto.id,
            dto.username,
            dto.displayName ? dto.displayName : dto.username,
            dto.email,
            dto.roles.map(role => UserRole[role]),
            UserType[dto.type]
        );
    }

    constructor(public id: string,
                public username: string,
                public displayName: string,
                public email: string,
                public roles: UserRole[],
                public type: UserType) {
    }

    public hasRole(role: UserRole): boolean {
        return this.roles.includes(role);
    }

    public isInternal(): boolean {
        return this.type == UserType.INTERNAL;
    }

    public isExternal(): boolean {
        return this.type == UserType.EXTERNAL;
    }

    public hasAdminRole(): boolean {
        return this.hasRole(UserRole.ADMIN);
    }

    public isAdminUser(): boolean {
        return this.username === ADMIN_USERNAME;
    }
}

export enum UserType {

    EXTERNAL = "EXTERNAL",

    INTERNAL = "INTERNAL"

}
