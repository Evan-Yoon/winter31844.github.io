import { getApp, getApps, initializeApp, type FirebaseApp } from 'firebase/app';
import { getAuth } from 'firebase/auth';
import { getDatabase } from 'firebase/database';

const firebaseConfig = {
  apiKey: import.meta.env.PUBLIC_FIREBASE_API_KEY,
  authDomain: import.meta.env.PUBLIC_FIREBASE_AUTH_DOMAIN,
  databaseURL: import.meta.env.PUBLIC_FIREBASE_DATABASE_URL,
  projectId: import.meta.env.PUBLIC_FIREBASE_PROJECT_ID,
  storageBucket: import.meta.env.PUBLIC_FIREBASE_STORAGE_BUCKET,
  messagingSenderId: import.meta.env.PUBLIC_FIREBASE_MESSAGING_SENDER_ID,
  appId: import.meta.env.PUBLIC_FIREBASE_APP_ID,
  measurementId: import.meta.env.PUBLIC_FIREBASE_MEASUREMENT_ID,
};

export function getFirebaseApp(name = '[DEFAULT]'): FirebaseApp {
  const existing = getApps().find((app) => app.name === name);
  if (existing) return existing;
  if (name === '[DEFAULT]' && getApps().length > 0) return getApp();
  return initializeApp(firebaseConfig, name);
}

export function getFirebaseDatabase(name = '[DEFAULT]') {
  return getDatabase(getFirebaseApp(name));
}

export function getFirebaseAuth(name = '[DEFAULT]') {
  return getAuth(getFirebaseApp(name));
}

