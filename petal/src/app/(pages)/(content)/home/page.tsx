import styles from "./layout.module.scss";
import SimpleHero from "@/app/components/Hero/SimpleHero";
import ColumnHero from "@/app/components/Hero/ColumnHero/ColumnHero";
import StepSection from "@/app/components/Section/Step/StepSection";
import ContactSection from "@/app/components/Section/Contact/ContactSection";
import ClientCardSection from "@/app/components/Section/ClientCard/ClientCardSection";
import boe from '@/app/assets/images/content/clientcard/BusinessOwnersExecutives.jpg';
import pr from '@/app/assets/images/content/clientcard/PreRetirees.jpg';
import pc from '@/app/assets/images/content/clientcard/PrivateClient.jpg';
import r from '@/app/assets/images/content/clientcard/Retirees.jpg';
import yp from '@/app/assets/images/content/clientcard/YoungProfessionals.jpg';
import fees from '@/app/assets/images/content/stepimage/fees.webp';
import plan from '@/app/assets/images/content/stepimage/plan.webp';
import promise from '@/app/assets/images/content/stepimage/promise.webp';
import testBanner from '@/app/assets/images/content/test-hero.jpg'
import StepImageSection from "@/app/components/Section/Step/StepImageSection";

/**
 * The home page
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function HomePage() {

  const baseClass: string = "home-page"

  //  TODO: responsive design
  //  TODO: back to top button

  //  RENDER

  return (
    <div className={styles[baseClass]}>
      <SimpleHero
        title={"Now you know a guy."}
        text={
          'Finances can be messy, money can be complicated and everyone seems to have the only advice you\'ll ever need to hear. We get it, we\'ve been there. ' +
          'Find out how simple things can be and empower your growth. Let your money work for you, so you can work on finding the next best ramen spot.'
        }
        variant={'image'}
        alignment={'center'}
        size={"large"}
        image={{
          src: testBanner,
          alt: 'Test Banner',
          height: 0,
          width: 0
        }}
        hasNavBar={true}
      />
      <StepImageSection
        title={"Finances can be simple, and beautiful."}
        elements={[
          {
            title: 'Big Picture, Bigger Savings',
            text: 'Our deep, in-depth approach covers every facet of your financial picture. We build a plan tailored to your goals and what your definition of financially independent is.',
            image: {
              src: promise,
              alt: 'Promise imaging'
            }
          },
          {
            title: 'Planning Geared to You',
            text: 'We build a challenging, yet realistic financial plan that covers what you want, in plain and simple language. No gimmicks, no fine print, no confusion.',
            image: {
              src: plan,
              alt: 'Plan imaging'
            }
          },
          {
            title: 'Plain and Simple Fees',
            text: 'What you see is what you pay. No hidden fees, no hidden charges. You will always know exactly what you\'ll be paying and to whom it will be payed.',
            image: {
              src: fees,
              alt: 'Fees imaging'
            }
          }
        ]}
      />
      <ClientCardSection
        title={'Your Goals, Our Mission'}
        subtitle={'No matter your stage in life & career, we\'re ready to serve you and your needs. Our goal is to support your pursuit of financial independence and to help you achieve your goals'}
        elements={[
          {
            title: 'Business Owners',
            text: 'I want to help my business continue to thrive, while also planning for retirement.',
            image: {
              src: boe,
              alt: 'Business Executives'
            }
          },
          {
            title: 'Families',
            text: 'I want to leverage the wealth I\'ve acquired to support my dependents and grow as a unit.',
            image: {
              src: pr,
              alt: 'Pre-Retirees'
            }
          },
          {
            title: 'Retirees',
            text: 'I am retired and want to protect my estate, ensure my succession and not run out of money in the process.',
            image: {
              src: r,
              alt: 'Retirees'
            }
          },
          {
            title: 'Private Clients',
            text: 'I possess a unique portfolio and require sophisticated solutions to complex problems.',
            image: {
              src: pc,
              alt: 'Private Clients'
            }
          },
          {
            title: 'Young Professionals',
            text: 'I\'m young and seeking to grow my assets, eliminate debt and set myself up for the future.',
            image: {
              src: yp,
              alt: 'Young Professionals'
            }
          }
        ]}
      />

      <StepSection
        title={'A realistic financial guide catered for you'}
        elements={[
          {
            title: 'Break It Down',
            text: 'We take a good, hard look at everything you own and owe. We\'ll walk you through everything that we see and everything we\'ve seen to paint your financial picture. You\'ll be surprised at how well you\'re doing and close you actually are to your goals.',
            image: {
              src: 'https://images.squarespace-cdn.com/content/v1/642b29f96b382f5bd2c745eb/4a42af0f-a9b8-4184-8e02-2df97899dcbe/Fee-only-planning.png',
              alt: 'default_image_1',
              width: 275,
              height: 144
            }
          },
          {
            title: 'Collaborative experience',
            text: 'You\'ll work one-on-one with a dedicated financial advisor to create a plan that will help you accomplish what matters most to you. A financial plan built by you, with you in mind with a little (or a lot) of our help.',
            image: {
              src: 'https://images.squarespace-cdn.com/content/v1/642b29f96b382f5bd2c745eb/ee65038f-07de-4c14-9538-3fad2b7e1229/collaborative-experience.png',
              alt: 'default_image_3',
              width: 275,
              height: 144
            }
          },
          {
            title: 'Targeted Investing',
            text: 'Your goals dictate your saving and investing strategy. Learn about what the market offers, how the market works and how you can extract the most value out of it to meet your goals. It is amazingly simple and wonderfully beautiful.',
            image: {
              src: 'https://images.squarespace-cdn.com/content/v1/642b29f96b382f5bd2c745eb/1ca79d9b-31e4-4561-9da1-f904a995d16c/goals-based-investing.png',
              alt: 'default_image_2',
              width: 275,
              height: 144
            }
          },
        ]}
      />
      <ColumnHero
        title={'Our Firm at a Glance'}
        elements={[
          {
            title: '$25.86B',
            text: 'Assets under management'
          },
          {
            title: '2024',
            text: 'year founded'
          },
          {
            title: '12+',
            text: 'professionals'
          }
        ]}
        disclaimer={'*Reflects combined AUM as of 3.31.2024 for bluebell and its subsidiaries.'}
        variant={"secondary"}
        size={"small"}
      />
      <ContactSection/>
    </div>
  );
}
