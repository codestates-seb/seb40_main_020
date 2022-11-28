import React, { lazy, Suspense } from 'react';
import { Routes, Route } from 'react-router-dom';
import Main from 'pages/Main/Main';
import Chatting from 'pages/Chatting';

const Balance = lazy(() => import('./pages/Balance'));
const History = lazy(() => import('./pages/History'));
const WaitOrders = lazy(() => import('./pages/WaitOrders'));
const Balances = lazy(() => import('./pages/Balances'));
const Exchange = lazy(() => import('./pages/Exchange'));
const MyPage = lazy(() => import('./pages/MyPage'));
const SignUp = lazy(() => import('./pages/SignUp'));
const Login = lazy(() => import('pages/Login'));
const FindPassword = lazy(() => import('pages/FindPassword'));
const ResetPassword = lazy(() => import('pages/ResetPassword'));
const Swap = lazy(() => import('pages/Swap'));
const SwapResult = lazy(() => import('pages/SwapResult'));

function App() {
	return (
		<>
			<Routes>
				<Route
					path="/"
					element={<Suspense fallback={<>...</>}>{<Main />}</Suspense>}
				/>
				<Route
					path="/exchange"
					element={<Suspense fallback={<>...</>}>{<Exchange />}</Suspense>}
				/>
				<Route
					path="/balances"
					element={<Suspense fallback={<>...</>}>{<Balances />}</Suspense>}
				/>
				<Route
					path="/investments/balance"
					element={<Suspense fallback={<>...</>}>{<Balance />}</Suspense>}
				/>
				<Route
					path="/investments/history"
					element={<Suspense fallback={<>...</>}>{<History />}</Suspense>}
				/>
				<Route
					path="/investments/wait_orders"
					element={<Suspense fallback={<>...</>}>{<WaitOrders />}</Suspense>}
				/>
				<Route
					path="/signup"
					element={<Suspense fallback={<>...</>}>{<SignUp />}</Suspense>}
				/>
				<Route
					path="/login"
					element={<Suspense fallback={<>...</>}>{<Login />}</Suspense>}
				/>
				<Route
					path="/findpassword"
					element={<Suspense fallback={<>...</>}>{<FindPassword />}</Suspense>}
				/>
				<Route
					path="/resetpassword"
					element={<Suspense fallback={<>...</>}>{<ResetPassword />}</Suspense>}
				/>
				<Route
					path="/swap"
					element={<Suspense fallback={<>...</>}>{<Swap />}</Suspense>}
				/>
				<Route
					path="/swapresult"
					element={<Suspense fallback={<>…</>}>{<SwapResult />}</Suspense>}
				/>
				<Route
					path="/mypage"
					element={<Suspense fallback={<>...</>}>{<MyPage />}</Suspense>}
				/>
			</Routes>
			<Chatting />
		</>
	);
}

export default App;
