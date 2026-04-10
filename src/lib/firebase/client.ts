import { getApp, getApps, initializeApp, type FirebaseApp } from 'firebase/app';
import { getAuth } from 'firebase/auth';
import { getDatabase } from 'firebase/database';

const firebaseConfig = {
  apiKey: 'AIzaSyCNkdX0A83XXEK-FIjqPt7b8iZxJ5mlw_4',
  authDomain: 'blog-stats-3b3fd.firebaseapp.com',
  databaseURL: 'https://blog-stats-3b3fd-default-rtdb.firebaseio.com',
  projectId: 'blog-stats-3b3fd',
  storageBucket: 'blog-stats-3b3fd.firebasestorage.app',
  messagingSenderId: '1009895082497',
  appId: '1:1009895082497:web:938bf796556e7889570e00',
  measurementId: 'G-Y6835HX2J5',
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

