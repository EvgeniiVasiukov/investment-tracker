export interface LoginRequest {
    email : string,
    password: string
}
export interface UserDto {
    id : number,
    email : string,
    role : string,
    status : string,
    createdAt : string
}
export interface LoginResponse {
    accessToken : string,
    tokenType : string,
    user : UserDto
}