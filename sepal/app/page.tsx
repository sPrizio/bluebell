import { redirect } from 'next/navigation'

/**
 * The default homepage
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function Home() {
  redirect('/trading-accounts');
}