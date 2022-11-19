import React, { lazy, Suspense } from 'react';
import { Routes, Route } from 'react-router-dom';
import SignUp from 'pages/SignUp/SignUp';
import Login from 'pages/Login/Login';
import FindPassword from 'pages/FindPassword/FindPassword';
import ResetPassword from 'pages/FindPassword/ResetPassword';

const Balance = lazy(() => import('./pages/Balance'));
const History = lazy(() => import('./pages/History'));
const WaitOrders = lazy(() => import('./pages/WaitOrders'));
const Exchange = lazy(() => import('./pages/Exchange'));

function App() {
	return (
		<>
			<Routes>
				<Route
					path="/"
					element={<Suspense fallback={<>...</>}>{<div>메인</div>}</Suspense>}
				/>
				<Route
					path="/exchange"
					element={<Suspense fallback={<>...</>}>{<Exchange />}</Suspense>}
				/>
				<Route
					path="/balances"
					element={<Suspense fallback={<>...</>}>{<>입출금</>}</Suspense>}
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
