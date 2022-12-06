import React, { lazy, Suspense } from 'react';
import { Routes, Route } from 'react-router-dom';
import Main from 'pages/Main/Main';
import Chatting from 'pages/Chatting';
import { useRecoilState } from 'recoil';
import { isLogin } from 'store';

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
const SignUpToken = lazy(() => import('pages/SignUpToken'));
const PasswordToken = lazy(() => import('pages/PasswordToken'));
import PrivateRoute from './components/PrivateRouter';
import Error from 'pages/Error';
import { getBalance } from 'api/balance';

function App() {
	const [isUseLogin, setIsUseLogin] = useRecoilState(isLogin);
	const Authorization = localStorage.getItem('login-token');
	if (Authorization) {
		getBalance().then(() => setIsUseLogin(true));
	}
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
					element={
						<PrivateRoute>
							<Suspense fallback={<>...</>}>{<Balances />}</Suspense>
						</PrivateRoute>
					}
				/>
				<Route
					path="/investments/balance"
					element={
						<PrivateRoute>
							<Suspense fallback={<>...</>}>{<Balance />}</Suspense>
						</PrivateRoute>
					}
				/>
				<Route
					path="/investments/history"
					element={
						<PrivateRoute>
							<Suspense fallback={<>...</>}>{<History />}</Suspense>
						</PrivateRoute>
					}
				/>
				<Route
					path="/investments/wait_orders"
					element={
						<PrivateRoute>
							<Suspense fallback={<>...</>}>{<WaitOrders />}</Suspense>
						</PrivateRoute>
					}
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
					element={
						<PrivateRoute>
							<Suspense fallback={<>...</>}>{<MyPage />}</Suspense>
						</PrivateRoute>
					}
				/>
				<Route
					path="/token/oauth2"
					element={<Suspense fallback={<>...</>}>{<SignUpToken />}</Suspense>}
				/>
				<Route
					path="/token/password"
					element={<Suspense fallback={<>...</>}>{<PasswordToken />}</Suspense>}
				/>
				<Route
					path="/*"
					element={<Suspense fallback={<>...</>}>{<Error />}</Suspense>}
				/>
			</Routes>
			<Chatting />
		</>
	);
}

export default App;
