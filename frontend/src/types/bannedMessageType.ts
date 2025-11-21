export type BannedMessageType = {
  userId: string;
  banned: boolean;
  bannedUntil: Date;
  amountOfInvalidRequests: number;
};
