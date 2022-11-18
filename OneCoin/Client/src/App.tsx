import React, { lazy, Suspense } from 'react';
import { Routes, Route } from 'react-router-dom';

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
					element={<Suspense fallback={<>...</>}>{<>보유 자산</>}</Suspense>}
				/>
				<Route
					path="/investments/history"
					element={<Suspense fallback={<>...</>}>{<>거래내역</>}</Suspense>}
				/>
				<Route
					path="/investments/wait_orders"
					element={<Suspense fallback={<>...</>}>{<>미체결</>}</Suspense>}
				/>
			</Routes>
		</>
	);
}

export default App;
