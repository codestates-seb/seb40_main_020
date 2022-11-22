import React, { lazy, Suspense } from 'react';
import { Routes, Route } from 'react-router-dom';
import SignUp from 'pages/SignUp/SignUp';
import Login from 'pages/Login/Login';
import FindPassword from 'pages/FindPassword/FindPassword';
import ResetPassword from 'pages/FindPassword/ResetPassword';
import Main from 'pages/Main/Main';
const Balance = lazy(() => import('./pages/Balance'));
const History = lazy(() => import('./pages/History'));
const WaitOrders = lazy(() => import('./pages/WaitOrders'));
const Balances = lazy(() => import('./pages/Balances'));
const Exchange = lazy(() => import('./pages/Exchange'));

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
					element={
						<Suspense fallback={<>...</>}>
							<SignUp />
						</Suspense>
					}
				/>
				<Route
					path="/login"
					element={
						<Suspense fallback={<>...</>}>
							<Login />
						</Suspense>
					}
				/>
				<Route
					path="/findpassword"
					element={
						<Suspense fallback={<>...</>}>
							<FindPassword />
						</Suspense>
					}
				/>
				<Route
					path="/resetpassword"
					element={
						<Suspense fallback={<>...</>}>
							<ResetPassword />
						</Suspense>
					}
				/>
			</Routes>
		</>
	);
}

export default App;
