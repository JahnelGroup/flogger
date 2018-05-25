#!/usr/bin/env bash
openssl aes-256-cbc -K $encrypted_939014b31d23_key -iv $encrypted_939014b31d23_iv -in cd/codesigning.asc.enc -out cd/codesigning.asc -d
gpg --fast-import codesigning.asc
