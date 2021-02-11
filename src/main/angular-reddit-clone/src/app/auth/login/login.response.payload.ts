export interface LoginResponsePayload {
    authenticationToken: string;
    expiresAt: string;
    refreshToken: string;
    username: string;
}