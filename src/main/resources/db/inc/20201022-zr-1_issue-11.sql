-- Added column for a salt which is used during encryption/decryption.
ALTER TABLE `connection` ADD `salt` varchar(255) DEFAULT '' after `password`;
